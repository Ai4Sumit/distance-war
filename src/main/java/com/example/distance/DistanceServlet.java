
package com.example.distance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String json = "{ "distance_km": " + String.format("%.2f", distance) + " }";

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String msg = e.getMessage() == null ? "Invalid input" : e.getMessage();
            String json = "{ "error": "" + escapeJson(msg) + "" }";
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(json);
        }
    }

    // ---- Utility methods ----
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private static String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        req.setCharacterEncoding("UTF-8");
        try (BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    // Minimal JSON number extractor for keys like "lat1": 12.34
    private static double extract(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\s*:\s*(-?\d+(?:\.\d+)?)");
        Matcher m = p.matcher(json);
        if (!m.find()) {
            throw new IllegalArgumentException("Missing or invalid '" + key + "'");
        }
        return Double.parseDouble(m.group(1));
    }

    private static void validateLatLon(double lat, double lon) {
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("Latitude out of range (-90..90)");
        if (lon < -180 || lon > 180) throw new IllegalArgumentException("Longitude out of range (-180..180)");
    }

    private static String escapeJson(String s) {
        return s.replace("\", "\\").replace(""", "\"");
    }
}
