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

import java.io.UnsupportedEncodingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GeneralMethods {

//	public static int getMaxSize(String tabla, String campo) {
//		int cant = 0;
//		Connection con = null;
//		try {
//			con = MysqlDAOFactory.obtenerConexion(GeneralVariables.nameDB);
//			DatabaseMetaData metadata = con.getMetaData();
//			ResultSet rst = metadata.getColumns(null, null, tabla, campo);
//			rst.next();
//			cant = rst.getInt("COLUMN_SIZE");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (con != null) {
//					con.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return cant;
//	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static void clearSession(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue(null);
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	public static int obtenerIndex(String ruta) {
		char[] c = ruta.toCharArray();
		int salida = 0;
		for (int i = c.length - 1; i >= 0; i--) {
			String help = c[i] + "";
			if (!help.equals("/")) {
				salida = i;
			} else {
				break;
			}
		}
		return salida;
	}

}
