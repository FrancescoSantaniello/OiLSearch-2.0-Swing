package francesco.santaniello.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConvertService extends ObjectMapper{

    private static class InnerClass{
        private static final JsonConvertService instance = new JsonConvertService();
    }

    private JsonConvertService(){}

    public static JsonConvertService getInstance(){
        return InnerClass.instance;
    }
}
