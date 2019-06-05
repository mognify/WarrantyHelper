public WarrantyAssist{
  public static void main(String[] args){
  
  }
  
  public static String getSheet(String sheetName, String sheetID){
  String strURL = "https://api.sheetson.com/v1/sheets/:sheetName?spreadsheetId=:spreadsheetId";
    URL url = new URL(strURL.replace(":sheetName",sheetName).replace(":spreadsheetId",sheetID);
    try(InputStream is = url.openStream(); JsonReader rdr = Json.createReader(is)){
      JsonObject obj = rdr.readObject();
      JsonArray results = obj.getJsonArray("data");
      for (JsonObject result : results.getValuesAs(JsonObject.class)) {
          System.out.print(result.getJsonObject("from").getString("name"));
          System.out.print(": ");
          System.out.println(result.getString("message", ""));
      }
    }
  }
}
