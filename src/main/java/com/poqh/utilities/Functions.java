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
package com.poqh.utilities;

import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Percy Oliver Quispe Huarcaya
 */
public  class Functions {
    @FunctionalInterface
    public static interface LoginFunction {
        /**
         * @param json1
         * @param json2
         * @param session
         * @throws Exception 
         */
        void call(JSONObject json1,JSONObject json2,HttpSession session) throws Exception;
    }
    
}
