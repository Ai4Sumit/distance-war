
package com.example.distance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Minimal servlet to calculate distance using Haversine formula.
 * Java 8 compatible.
 */
public class DistanceServlet extends HttpServlet {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String body = readBody(request);

        try {
            double lat1 = getValue(body, "lat1");
            double lon1 = getValue(body, "lon1");
            double lat2 = getValue(body, "lat2");
            double lon2 = getValue(body, "lon2");

            double distance = haversine(lat1, lon1, lat2, lon2);

            String json = "{ \"distance_km\": " + String.format("%.2f", distance) + " }";

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    "{ \"error\": \"Invalid input\" }"
            );
        }
    }

    // ---------------- helper methods ----------------

    private static double haversine(
            double lat1, double lon1,
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

    private static String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    // VERY simple extraction to avoid regex complexity
    private static double getValue(String body, String key) {
        int i = body.indexOf(key);
        if (i == -1) throw new IllegalArgumentException();
        int colon = body.indexOf(":", i);
        int comma = body.indexOf(",", colon);
        if (comma == -1) {
            comma = body.indexOf("}", colon);
        }
        return Double.parseDouble(
                body.substring(colon + 1, comma).trim()
        );
    }
}
