/*
 * Copyright 2020 the original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openapiprocessor.core.processor.mapping.v2

import io.openapiprocessor.core.converter.mapping.*
import io.openapiprocessor.core.model.HttpMethod
import io.openapiprocessor.core.processor.mapping.v2.Mapping as MappingV2
import io.openapiprocessor.core.processor.mapping.v2.parser.ToData
import io.openapiprocessor.core.processor.mapping.v2.parser.ToExtractor
import io.openapiprocessor.core.processor.mapping.v2.parser.ToLexer
import io.openapiprocessor.core.processor.mapping.v2.parser.ToParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

/**
 *  Converter for the type mapping from the mapping yaml. It converts the type mapping information
 *  into the format used by [io.openapiprocessor.core.converter.DataTypeConverter].
 *
 *  @author Martin Hauner
 */
class MappingConverter(val mapping: MappingV2) {
    companion object {
        private const val SEPARATOR_TYPE = " => "
        private const val SEPARATOR_FORMAT = ":"
        private val PATTERN_GENERICS = "(\\S+?)\\s*<(.+?)>".toPattern()
    }

    fun convert(): List<Mapping> {
        val result = ArrayList<Mapping>()

        if(mapping.map.result != null) {
            result.add(convertResult(mapping.map.result))
        }

        if(mapping.map.single != null) {
            result.add(convertType("single" , mapping.map.single))
        }

        if(mapping.map.multi != null) {
            result.add(convertType("multi", mapping.map.multi))
        }

        //if(mapping.map.`null` != null) {
        //    result.add(convertNull(mapping.map.`null`))
        //}

        mapping.map.types.forEach {
            result.add(convertType(it))
        }

        mapping.map.parameters.forEach {
            result.add (convertParameter (it))
        }

        mapping.map.responses.forEach {
            result.add (convertResponse (it))
        }

        mapping.map.paths.forEach {
            result.add(convertPath (it.key, it.value))
            result.addAll(convertPathMethods(it.key, it.value))
        }

        return result
    }

    private fun convertResult (result: String): Mapping {
        return ResultTypeMapping(result)
    }

    private fun convertNull(value: String): Mapping {
        val split = value
                .split(" = ")
                .map { it.trim() }

        val type = split.component1()
        var init: String? = null
        if (split.size == 2)
            init = split.component2()

        return NullTypeMapping("null", type, init)
    }

    private fun convertType (from: String, to: String): Mapping {
        return TypeMapping(from, to)
    }

    private fun convertType(source: Type): Mapping {
        val (fromType, toType) = splitMapping(source.type)
        val (fromName, fromFormat) = parseFromType(fromType)
        val (toName, generics) = parseToType(toType, source.generics)

        return TypeMapping(fromName, fromFormat, toName, generics)
    }

    private fun convertParameter(source: Parameter): Mapping {
        return when (source) {
            is RequestParameter -> {
                createParameterTypeMapping(source)
            }
            is AdditionalParameter -> {
                createAddParameterTypeMapping(source)
            }
            else -> {
                throw Exception("unknown parameter mapping $source")
            }
        }
    }

    private fun createParameterTypeMapping(source: RequestParameter): ParameterTypeMapping {
        val (name, toType) = splitMapping(source.name)
        val (toName, generics) = parseToType(toType, source.generics)

        return ParameterTypeMapping(name, TypeMapping(null, toName, generics))
    }

    private fun createAddParameterTypeMapping(source: AdditionalParameter): AddParameterTypeMapping {
        val (name, toType) = splitMapping(source.add)
        val to = parseToTypeV2(toType, source.generics)

        return AddParameterTypeMapping(name, to.createSourcelessTypeMapping(), to.createAnnotation())
    }

    private fun convertResponse(source: Response): Mapping {
        val (content, toType) = splitMapping(source.content)
        val (toName, generics) = parseToType(toType, source.generics)

        return ResponseTypeMapping(content, TypeMapping(null, toName, generics))
    }

    private fun convertPath(path: String, source: Path): Mapping {
        val result = ArrayList<Mapping>()

        if(source.result != null) {
            result.add(convertResult(source.result))
        }

        if(source.single != null) {
            result.add(convertType("single" , source.single))
        }

        if(source.multi != null) {
            result.add(convertType("multi", source.multi))
        }

        if(source.`null` != null) {
            result.add(convertNull(source.`null`))
        }

        source.types.forEach {
            result.add(convertType(it))
        }

        source.parameters.forEach {
            result.add (convertParameter (it))
        }

        source.responses.forEach {
            result.add (convertResponse (it))
        }

        return EndpointTypeMapping(path, null, result, source.exclude)
    }

    private fun convertPathMethods(path: String, source: Path): List<Mapping> {
        val result = ArrayList<Mapping>()

        if (source.get != null) {
            result.add(convertPathMethod(path, HttpMethod.GET, source.get))
        }

        if (source.put != null) {
            result.add(convertPathMethod(path, HttpMethod.PUT, source.put))
        }

        if (source.post != null) {
            result.add(convertPathMethod(path, HttpMethod.POST, source.post))
        }

        if (source.delete != null) {
            result.add(convertPathMethod(path, HttpMethod.DELETE, source.delete))
        }

        if (source.options != null) {
            result.add(convertPathMethod(path, HttpMethod.OPTIONS, source.options))
        }

        if (source.head != null) {
            result.add(convertPathMethod(path, HttpMethod.HEAD, source.head))
        }

        if (source.patch != null) {
            result.add(convertPathMethod(path, HttpMethod.PATCH, source.patch))
        }

        if (source.trace != null) {
            result.add(convertPathMethod(path, HttpMethod.TRACE, source.trace))
        }

        return result
    }

    private fun convertPathMethod(path: String, method: HttpMethod, source: PathMethod): Mapping {
        val result = ArrayList<Mapping>()

        if(source.result != null) {
            result.add(convertResult(source.result))
        }

        if(source.single != null) {
            result.add(convertType("single" , source.single))
        }

        if(source.multi != null) {
            result.add(convertType("multi", source.multi))
        }

        if(source.`null` != null) {
            result.add(convertNull(source.`null`))
        }

        source.types.forEach {
            result.add(convertType(it))
        }

        source.parameters.forEach {
            result.add (convertParameter (it))
        }

        source.responses.forEach {
            result.add (convertResponse (it))
        }

        return EndpointTypeMapping(path, method, result, source.exclude)
    }

    private data class MappingTypes(val result: String, val format: String)
    private data class FromType(val name: String, val format: String?)
    private data class ToType(val name: String, val generics: List<String>)

    private fun splitMapping(type: String): MappingTypes {
        val split = type
                .split(SEPARATOR_TYPE)
                .map { it.trim() }

        return MappingTypes(
                split.component1(),
                split.component2())
    }

    private fun parseFromType(type: String): FromType {
        var name = type
        var format: String? = null

        if (type.contains(SEPARATOR_FORMAT)) {
            val split = type
                    .split(SEPARATOR_FORMAT)
                    .map { it.trim() }

            name = split.component1()
            format = split.component2()
        }

        return FromType(name, format)
    }

    private fun parseToType(type: String, typeGenerics: List<String>?): ToType {
        var name: String = type
        var generics = emptyList<String>()

        val matcher = PATTERN_GENERICS.matcher(type)
        if (matcher.find ()) {
            name = matcher.group (1)
            generics = matcher
                .group (2)
                .split (',')
                    .map { it.trim() }
                    .toList()

        } else if (typeGenerics != null) {
            generics = typeGenerics
        }

        return ToType(name, resolvePackageVariable(generics))
    }

    private fun resolvePackageVariable(source: List<String>): List<String> {
        return source.map {
            it.replace("{package-name}", mapping.options.packageName)
        }
    }

}

private fun ToData.createSourcelessTypeMapping() =
    TypeMapping(null, type, typeArguments)

private fun ToData.createAnnotation(): io.openapiprocessor.core.converter.mapping.Annotation? {
    if(annotationType == null) {
        return null
    }

    return Annotation(annotationType!!, annotationParameters)
}

/**
 * parse "to" with grammar
 */
private fun parseToTypeV2(type: String, typeGenerics: List<String>?): ToData {
    val lexer = ToLexer(CharStreams.fromString(type))
    val tokens = CommonTokenStream(lexer)
    val parser = ToParser(tokens)
    val ctx = parser.to()
    val extractor = ToExtractor()
    ParseTreeWalker().walk(extractor, ctx)

    val target = extractor.getTarget()
    if (typeGenerics != null) {
        target.typeArguments = typeGenerics
    }
    return target
}
