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
package com.poqh.config;

import com.poqh.utilities.OsUtils;
import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author Percy Oliver Quispe Huarcaya Clase encargada de almacenar las Url
 * para enviar las solicitudes al servicio rest
 */
public final class RequestPath {

    /*REST PRODUCCION*/
    private static final String BASE = getBase();  //base path;
    /*Rest seguridad (security-rest)*/
    public static final String LOG_OUT = BASE + "user/logout";
    public static final String LOGIN = BASE + "user/login";
    public static final String ENVIAR_NUEVO_TOKEN = BASE + "proyecto/enviarNuevoToken";
    public static final String VERIFICAR_LOGIN = BASE + "user/verificarLogin";
    
    public static final String getBase(){
        final Dotenv dotenv = Dotenv
            .configure()
            .directory(OsUtils.getDotEnvPath("security"))
            .load();
        return dotenv.get("BASE");
    }
}
