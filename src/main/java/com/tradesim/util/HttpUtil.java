package com.tradesim.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpUtil {
  public static String get(String url) {
    try {
      HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setRequestMethod("GET");
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      return in.lines().collect(Collectors.joining());
    } catch (Exception e) {
      e.printStackTrace();
      return "{}";
    }
  }
}