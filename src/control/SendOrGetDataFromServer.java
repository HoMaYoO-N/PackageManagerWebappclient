package control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gson.extras.RuntimeTypeAdapterFactory;
import model.*;
import model.Package;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SendOrGetDataFromServer {

    public static String GetStringJSONFromPackage(Package aPackage)
    {
        Type type = new TypeToken<ArrayList<Package>>(){}.getType();
        RuntimeTypeAdapterFactory<Package> packageAdapterFactory = RuntimeTypeAdapterFactory
                .of(Package.class, "type")
                .registerSubtype(Book.class,"Book")
                .registerSubtype(Perishable.class, "Perishable")
                .registerSubtype(Electronic. class,"Electronic");

        Gson g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                        new TypeAdapter<LocalDateTime>() {
                            @Override
                            public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                                DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                jsonWriter.value(localDateTime.format(formatObj).toString());
                            }
                            @Override
                            public LocalDateTime read(JsonReader jsonReader) throws IOException {
                                DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                return LocalDateTime.parse(jsonReader.nextString(),formatObj);
                            }
                        }).registerTypeAdapterFactory(packageAdapterFactory)
                .create();
        return g.toJson(aPackage);
    }

    public static String GetJSONResultOfCurlCommandFromServer(String command) throws IOException {
        //some part about reading input retrieved from here:
        // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
        Process process = Runtime.getRuntime().exec(command);
        InputStream stream = process.getInputStream();
        String result = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
        return result;
    }
    private static List<Package> ParseJSONToGetPackageList(String result) throws Exception {
        Type type = new TypeToken<ArrayList<Package>>(){}.getType();
        RuntimeTypeAdapterFactory<Package> packageAdapterFactory = RuntimeTypeAdapterFactory
                .of(Package.class, "type")
                .registerSubtype(Book.class,"Book")
                .registerSubtype(Perishable.class, "Perishable")
                .registerSubtype(Electronic. class,"Electronic");

        Gson g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                        new TypeAdapter<LocalDateTime>() {
                            @Override
                            public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                                jsonWriter.value(localDateTime.toString());
                            }
                            @Override
                            public LocalDateTime read(JsonReader jsonReader) throws IOException {
                                DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                return LocalDateTime.parse(jsonReader.nextString(),formatObj);
                            }
                        }).registerTypeAdapterFactory(packageAdapterFactory)
                .create();
        if(g.fromJson(result, type) == null)
        {
            throw new Exception();
        }
        List<Package> packageList = g.fromJson(result, type);

        //fix the type variable
        for (int i = 0; i < packageList.size();i++)
        {
            String className = String.valueOf(packageList.get(i).getClass());
            if(className.contains("Book"))
                packageList.get(i).setType("Book");
            if(className.contains("Electronic"))
                packageList.get(i).setType("Electronic");
            if(className.contains("Perishable"))
                packageList.get(i).setType("Perishable");
        }
        return packageList;
    }

    public static List<Package> GetCustomPackageListFromServer(String command)
    {

        try {
            String result = GetJSONResultOfCurlCommandFromServer(command);
            List<Package> packageList = ParseJSONToGetPackageList(result);
            return packageList;
        } catch (IOException e) {
            System.out.println("There is a problem reading this cURL command. It does not make sense!");
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("JSON Data Received from WebServer was null so probably the WebServer was not running to begin with.");
            throw new RuntimeException(e);
        }
    }

    public static void AddPackageToServer(Package newPackage, String type)
    {
        String json = SendOrGetDataFromServer.GetStringJSONFromPackage(newPackage);
        String jsonPartReplaced = json.replaceAll("\"","\\\\" + "\"");
        String command = "curl -i -H \"Content-Type:application/json\" -X POST -d"+ "\"" + jsonPartReplaced + "\"" +" "+" "+"localhost:8080/add" + type;
        try {
            SendOrGetDataFromServer.GetJSONResultOfCurlCommandFromServer(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void RemovePackageFromServer(int packageNumber)
    {
        String command = "curl -i -H \"Content-Type:application/json\" -X POST localhost:8080/removePackage/" + packageNumber;
        try {
            SendOrGetDataFromServer.GetJSONResultOfCurlCommandFromServer(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void SaveJSONInServer()
    {
        String command = "curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/exit";
        try {
            SendOrGetDataFromServer.GetJSONResultOfCurlCommandFromServer(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void ChangeIsDeliveredStatus(int packageNumber)
    {
        String command = "curl -i -H \"Content-Type:application/json\" -X POST localhost:8080/markPackageAsDelivered/" + packageNumber;
        try {
            SendOrGetDataFromServer.GetJSONResultOfCurlCommandFromServer(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
