package greenbot.main.terraform;

import java.io.File;

import com.microsoft.terraform.TerraformClient;

import lombok.SneakyThrows;

public class TerraformUtils {

	@SneakyThrows
	public static void apply(String path) {
		try (TerraformClient client = new TerraformClient()) {
			client.setOutputListener(System.out::println);
			client.setErrorListener(System.err::println);

			client.setWorkingDirectory(new File(path));
			client.apply().get();
		}
	}

	@SneakyThrows
	public static void destroy(String path) {
		try (TerraformClient client = new TerraformClient()) {
			client.setOutputListener(System.out::println);
			client.setErrorListener(System.err::println);

			client.setWorkingDirectory(new File(path));
			client.destroy().get();
		}
	}

}
