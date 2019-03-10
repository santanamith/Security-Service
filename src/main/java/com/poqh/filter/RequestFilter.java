/*
 * Copyright 2019 Percy Oliver Quispe Huarcaya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poqh.filter;

import org.json.JSONObject;

/**
 *
 * @author Percy Oliver Quispe Huarcaya
 */
public class RequestFilter {
    /**
     * @param body
     * @return status 
     */
    public JSONObject indice(JSONObject body){
        JSONObject salida = new JSONObject();
        salida.put("status", true);
        return salida;
    }
}
