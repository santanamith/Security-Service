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

import com.poqh.config.RequestPath;
import com.poqh.utilities.Functions.LoginFunction;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Percy Oliver Quispe Huarcaya
 */
public class Testing {
    public static void main(String[] args) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        JSONObject obj = new JSONObject();
        obj.put("usuario", "pquispe");
        obj.put("pass", "123456");
        obj.put("codigoProyecto", "1");
        String r = httpRequest.getRespuesta(RequestPath.LOGIN, HttpRequest.POST, obj, "");//Respuesta del server
        JSONObject respuesta = new JSONObject(r);
        JSONObject data = new JSONObject(respuesta.get("data").toString());
        JSONObject dataPersonal = new JSONObject(respuesta.get("dataPersonal").toString());
        String nombre = data.getString("nombrePersona") + " " + data.getString("apellidoPaterno");
        JSONArray datosUsuario = dataPersonal.getJSONArray("datosUsuario");
        String roles = "";
        String codigo = "";
        String codigoTipoUsuario = "";
        for (int i = 0; i < datosUsuario.length(); i++) {
            JSONObject usuario = (JSONObject) datosUsuario.get(i);
            roles += usuario.getString("nombreTipoUsuario") + " - ";
            codigoTipoUsuario += usuario.getString("codigoTipoUsuario")+",";
            codigo = usuario.getString("codigoUsuario");
        }
        System.out.println(codigoTipoUsuario.substring(0,codigoTipoUsuario.length()-1));
        System.out.println(roles);
        System.out.println(codigo);
        System.out.println(r);
        SecurityAlternative s = new SecurityAlternative();
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        LoginFunction f = new Functions.LoginFunction() {
            @Override
            public void call(JSONObject json1, JSONObject json2,HttpSession session) throws Exception {
                
            }
        };
        s.login(request, response, f);
    }
}
