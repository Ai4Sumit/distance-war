
package com.example.distance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServlet double lon1 = extract(body, "lon1");
            double lat2 = extract(body, "lat2");
            double lon2 = extract(body, "lon2");

            validate(lat1, lon1);
            validate(lat2, lon2);

            double distance = haversine(lat1, lon1, lat2, lon2);

            String json =
                    "{ \"distance_km\": " + String.format("%.2f", distance) + " }";

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);

        } catch (Exception e) {
            String json =
                    "{ \"error\": \"" + escape(e.getMessage()) + "\" }";

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(json);
        }
    }

    /* ================= Utility Methods ================= */

    private static double haversine(
            double lat1, double lon1,
           (Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private static String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static double extract(String json, String key) {
        Pattern pattern =
                Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(json);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing or invalid '" + key + "'");
        }
        return Double.parseDouble(matcher.group(1));
    }

    private static void validate(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude out of range (-90..90)");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude out of range (-180..180)");
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
