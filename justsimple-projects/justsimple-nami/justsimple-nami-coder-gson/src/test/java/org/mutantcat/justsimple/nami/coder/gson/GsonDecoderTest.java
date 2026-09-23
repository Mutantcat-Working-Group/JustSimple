package org.mutantcat.justsimple.nami.coder.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.nami.Decoder;
import org.mutantcat.justsimple.nami.Result;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class GsonDecoderTest {

	Decoder decoder=GsonDecoder.instance;
	Gson gson=new Gson();
	@Test
	void testDecodeObject() throws Throwable {
		User user=new User(100, "zhang", "张三");
		Result rst=new Result(200,gson.toJson(user).getBytes());		
		User object= (User)decoder.decode(rst, User.class);
		assertNotNull(object);
		assertEquals(user, object);
	}
	@Test
	void testDecodeList() throws Throwable {
		List<User> users=new ArrayList<>();
		users.add(new User(100, "zhang", "张三"));
		users.add(new User(200, "wang", "王东"));
		
		Result rst=new Result(200,gson.toJson(users).getBytes());		
		Type type=new TypeToken<List<User>>() {}.getType();
		@SuppressWarnings("unchecked")
		List<User> object= (List<User>)decoder.decode(rst, type);
		assertNotNull(object);
		assertEquals(users.size(), object.size());
		assertEquals(rst.bodyAsString(), gson.toJson(object));
	}
}
