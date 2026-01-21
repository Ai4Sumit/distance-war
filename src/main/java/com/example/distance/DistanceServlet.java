package com.example.distance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
importTH_RADIUS_KM = 6371.0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String body = readBody(request);

        try {
            double lat1 = extract(body, "lat1");
            double lon1 = extract(body, "lon1");
            double lat2 = extract(body, "lat2");
            double lon2 = extract(body, "lon2");

            validate(lat1, lon1);
            validate(lat2, lon2);

            double distance = haversine(lat1, lon1, lat2, lon2);

            String json = "{ \"distance_km\": " +
                    String.format("%.2f", distance) + " }";

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);

        } catch (Exception e) {
            String json = "{ \"error\": \"" + escape(e.getMessage()) + "\" }";
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(json);
        }
    }

    // ---------- helpers ----------

    private static double haversine(double lat1, double lon1,
                                    double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static double extract(String json, String key) {
        Pattern p = Pattern.compile(
                "\"" + key + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        invalid '" + key + "'");
        }
        return Double.parseDouble(m.group(1));
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
