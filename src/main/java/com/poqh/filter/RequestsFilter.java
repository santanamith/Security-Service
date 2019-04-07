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

import com.poqh.utilities.CallFunction;
import static com.poqh.utilities.GeneralMethods.obtenerIndex;
import com.poqh.utilities.HttpRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.poqh.config.RequestPath;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Percy Oliver Quispe Huarcaya
 */
public final class RequestsFilter {
    public void doBeforeProcessing(ServletRequest req, ServletResponse resp, FilterChain chain,CallFunction<List> funcion)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String uri = request.getRequestURI();
        if (uri.endsWith("vistas/index.jsp") || uri.endsWith("vistas/")) {
            if (request.getSession().getAttribute("codigo") != null) {
                response.sendRedirect("main.jsp");
                return;
            }
            chain.doFilter(req, resp);
        } else {
            if (request.getSession().getAttribute("codigo") != null) {
                if (uri.endsWith(".jsp")) {
                    HttpSession session = request.getSession();
                    HttpRequest httpRequest = new HttpRequest();
                    JSONObject valid = null;
                    String respuesta = "";
                    String auth = "";
                    try {
                        if (session.getAttribute("Authorization") != null) {
                            auth = (String) session.getAttribute("Authorization");
                        } else {
                            sendError(response);
                        }
                        respuesta = httpRequest.getRespuesta(RequestPath.VERIFICAR_LOGIN, HttpRequest.POST, new JSONObject("{}"), auth);
                        valid = new JSONObject(respuesta);
                        if (valid.getBoolean("status")) {
                            JSONObject menu = new JSONObject(valid.getString("menu"));//Obtiene el menu
                            List<Object> vistas = new ArrayList<>();
                            JSONObject rolvista = valid.getJSONObject("rolvista");
                            JSONArray urls = rolvista.getJSONArray("vistas");
                            for (int i = 0; i < urls.length(); i++) {
                                vistas.add(urls.get(i));
                            }
                            vistas.add("main.jsp");
                            if(funcion!=null){
                                funcion.call(vistas);
                            }
                            String ruta = request.getRequestURI();
                            int indice = obtenerIndex(ruta);
                            String rutaJsp = ruta.substring(indice, ruta.length());
                            boolean acceso = vistas.contains(rutaJsp);
                            if (!acceso) {
                                request.getSession().setAttribute("error", "no tiene acceso a la vista solicitada");//Esta session se elimina en el jsp (para que no ocupe memoria)
                                request.getRequestDispatcher("/vistas/templates/error.jsp").forward(request, response);
                                return;
                            }
                            session.setAttribute("menu", menu.toString());
                        } else {
                            deleteCredenciales(response, request);
                            request.getSession().setAttribute("error", "no tiene credenciales validas");
                            request.getRequestDispatcher("/vistas/templates/error.jsp").forward(request, response);
                            return;
                        }
                    } catch (Exception ex) {
                        request.getSession().setAttribute("error", ex.getMessage());
                        request.getRequestDispatcher("/vistas/templates/error.jsp").forward(request, response);
                        return;
                    }
                }
                chain.doFilter(request, response);
            } else {
                deleteCredenciales(response, request);
                response.sendRedirect("../vistas/index.jsp");
            }
        }
    }

    private  void deleteCredenciales(HttpServletResponse response, HttpServletRequest request) {
        request.getSession().invalidate();
        Cookie cookieAuth = new Cookie("Authorization", "");
        cookieAuth.setMaxAge(0);
        response.addCookie(cookieAuth);
    }

    private  void sendError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendError(401);
    }


}
