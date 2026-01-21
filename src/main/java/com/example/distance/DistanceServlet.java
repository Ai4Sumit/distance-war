
package com.example.distance;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DistanceServlet extends HttpServlet {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String body = readBody(req);

        try {
            double lat1 = extract(body, "lat1");
            double lon1 = extract(body, "lon1");
            double lat2 = extract(body, "lat2");
            double lon2 = extract(body, "lon2");

            validateLatLon(lat1, lon1);
            validateLatLon(lat2, lon2);

            double distance = haversine(lat1, lon1, lat2, lon2);

            String json =
                "{ \"distance_km\": " + String.format("%.2f", distance) + " }";

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);

        } catch (Exception e) {
            String json =
                "{ \"error\": \"" + escapeJson(e.getMessage()) + "\" }";

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(json);
        }
    }

    // ---------------- Utility methods ----------------

    private static double haversine(double lat1, double lon1,
                                    double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            a));
        return EARTH_RADIUS_KM * c;
    }

    private static String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    private }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
