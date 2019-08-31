/*
 * Copyright 2019 the original authors
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

package com.github.hauner.openapi.spring.model.datatypes;

import java.util.Set;

/**
 * OpenAPI type 'array' maps to Collection<>.
 *
 * @author Martin Hauner
 */
public class CollectionDataType implements DataType {

    private DataType item

    @Override
    public String getName () {
        "Collection<${item.name}>"
    }

    @Override
    public String getPackageName () {
        "java.util"
    }

    @Override
    public Set<String> getImports () {
        ['java.util.Collection'] + item.imports
    }

}