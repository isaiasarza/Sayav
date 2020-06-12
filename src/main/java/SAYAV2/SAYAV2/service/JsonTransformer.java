package SAYAV2.SAYAV2.service;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer{

	private Gson gson;
	
	
	 
    public JsonTransformer() {
		super();
		this.gson = new Gson();
	}

   
	@Override
    public String render(Object model) {
        return gson.toJson(model);
    }

	public Gson getGson() {
		return gson;
	}

}
