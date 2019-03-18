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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Percy Oliver Quispe Huarcaya
 */
public class SecurityAlternative {
    public void login(HttpServletRequest request, HttpServletResponse response,Functions.LoginFunction f) {
        String body = request.getParameter("body");
        if (body != null) {
            ResponseHelper responseHelper = new ResponseHelper();
            JSONObject salida = null; //Json para enviar al browser
            JSONObject obj = new JSONObject(body);
            JSONObject respuesta = null;//Adquiere los datos de la respuesta del servidor
            HttpRequest httpRequest = new HttpRequest();
            try {
                String r = httpRequest.getRespuesta(RequestPath.LOGIN, HttpRequest.POST, obj, "");//Respuesta del server
                respuesta = new JSONObject(r);
                boolean status = respuesta.getBoolean("status");
                responseHelper.setStatus(status);

                if (status) {
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
                    /*Sesionando credenciales requeridas*/
                    HttpSession session = request.getSession();
                    if(f!=null){
                        f.call(data, dataPersonal,session);
                    }
                    session.setAttribute("usuario", obj.getString("usuario"));
                    session.setAttribute("codigo", codigo);
                    session.setAttribute("nombre", nombre);
                    session.setAttribute("roles", roles.substring(0, roles.length() - 2));
                    session.setAttribute("codigoTipoUsuario", codigoTipoUsuario.substring(0,codigoTipoUsuario.length()-1));
                    session.setAttribute("Authorization", "Bearer " + respuesta.getString("token"));
                    session.setMaxInactiveInterval(120 * 60);

                    Cookie cookieAuth = new Cookie("Authorization", "Bearer " + respuesta.getString("token"));
                    cookieAuth.setMaxAge(36000);//10horas
                    response.addCookie(cookieAuth);
                } else {
                    responseHelper.setMessage(respuesta.getString("message"));
                }
                salida = new JSONObject(responseHelper);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.print(salida);
            } catch (Exception ex) {
                responseHelper.setStatus(false);
                responseHelper.setMessage("Error " + ex.getMessage());
            }
        }
    }

    public void interceptar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("codigo") != null && session.getAttribute("Authorization") != null) {
            String token = (String) session.getAttribute("Authorization");
            String codigoProyecto = request.getParameter("cp");
            String page = request.getParameter("p");
            String ruta = page + "?cp=" + codigoProyecto + "&t=" + token;
            response.sendRedirect(ruta);
        } else {
            request.getRequestDispatcher("/vistas/index.jsp").forward(request, response);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException, ServletException {
        if (request.getSession().getAttribute("codigo") != null && !request.getSession().getAttribute("codigo").toString().trim().equals("")) {
            HttpSession session = request.getSession();
            String authorization = (String) session.getAttribute("Authorization");
            HttpRequest postRequest = new HttpRequest();
            String respuesta = postRequest.getRespuesta(RequestPath.LOG_OUT, HttpRequest.POST, new JSONObject("{}"), authorization);
            GeneralMethods.clearSession(request, response);
            request.getSession().removeAttribute("codigo");//
            request.getSession().removeAttribute("Authorization");
            request.getSession().removeAttribute("menu");
            request.getRequestDispatcher("/vistas/index.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/vistas/index.jsp").forward(request, response);
        }
    }

    public void redireccionar(HttpServletRequest request, HttpServletResponse response, Functions.LoginFunction f) throws IOException, Exception  {
        if (request.getParameter("t") != null && request.getParameter("cp") != null) {
            String auth = request.getParameter("t");
            if (auth.startsWith("Bearer ")) {
                String codigoProyecto = request.getParameter("cp");
                HttpRequest httpRequest = new HttpRequest();
                String r = httpRequest.getRespuesta(RequestPath.ENVIAR_NUEVO_TOKEN, HttpRequest.POST, new JSONObject("{codigoProyecto:" + codigoProyecto + "}"), auth);
                JSONObject respuesta = new JSONObject(r);
                if (respuesta.getBoolean("status")) {
                    HttpSession session = request.getSession();
                    Cookie cookieAuth = null;
                    JSONObject data = new JSONObject(respuesta.get("data").toString());
                    JSONObject dataPersonal = new JSONObject(respuesta.get("dataPersonal").toString());
                    String nombre = data.getString("apellidoPaterno") + " " + data.getString("apellidoMaterno") + " "
                            + data.getString("nombrePersona");
                    JSONArray datosUsuario = dataPersonal.getJSONArray("datosUsuario");
                    String roles = "";
                    String codigo = "";
                    String user = "";
                    for (int i = 0; i < datosUsuario.length(); i++) {
                        JSONObject usuario = (JSONObject) datosUsuario.get(i);
                        roles += usuario.getString("nombreTipoUsuario") + " - ";
                        codigo = usuario.getString("codigoUsuario");
                        user = usuario.getString("usuario");
                    }
                    if(f!=null){
                        f.call(data, dataPersonal,session);
                    }
                    session.setAttribute("usuario", user);
                    session.setAttribute("codigo", codigo);
                    session.setAttribute("nombre", nombre);
                    session.setAttribute("roles", roles.substring(0, roles.length() - 2));
                    session.setAttribute("Authorization", "Bearer " + respuesta.getString("token"));
                    session.setMaxInactiveInterval(120 * 60);
                    cookieAuth = new Cookie("Authorization", "Bearer " + respuesta.getString("token"));
                    cookieAuth.setMaxAge(36000);//10horas
                    response.addCookie(cookieAuth);
                    response.sendRedirect("main.jsp");
                    return;
                }
            }
        }
        Cookie cookieAuth = new Cookie("Authorization", "");
        cookieAuth.setMaxAge(0);
        response.addCookie(cookieAuth);
        request.getRequestDispatcher("/vistas/index.jsp").forward(request, response);
    }

    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String authorization = (String) request.getSession().getAttribute("Authorization");
        JSONObject auth = new JSONObject();
        auth.put("Authorization", authorization);
        auth.put("status", true);
        PrintWriter pw = response.getWriter();
        pw.print(auth);
    }
}

