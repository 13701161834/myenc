package enc;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MyEnc {

	/**
	 * @param args
	 * @throws IOException
	 */

	public static int enc(String file, String psw) throws IOException {
		int result = 1;
		if (file.endsWith(".x")) {
			return result;
		}
		// if dir
		File orgFile = new File(file);

		if (orgFile.isFile()) {
			String outFile = file + ".x";
			xor(file, outFile, psw);
			result = 0;
		} else {
			if (orgFile.isDirectory()) {
				String[] subfilenames = orgFile.list();
				for (String subfilename : subfilenames) {
					enc(orgFile.getPath() + "\\" + subfilename, psw);
				}
				result = 0;
			} else {
				// *,?

				result = 1;
			}
		}

		return result;
	}

	public static int deEnc(String file, String psw) throws IOException {
		int result = 1;
		// if dir
		File orgFile = new File(file);

		if (orgFile.isFile()) {
			if (file.endsWith(".x")) {
				// - .x

				String outFile = file.substring(0, file.length() - 2);
				// System.out.println(outFile);
				xor(file, outFile, psw);
				result = 0;
			}
		} else {
			if (orgFile.isDirectory()) {
				String[] subfilenames = orgFile.list();
				for (String subfilename : subfilenames) {
					deEnc(orgFile.getPath() + "\\" + subfilename, psw);
				}
				result = 0;
			} else {
				// *,?

				result = 1;
			}
		}
		// System.out.println("result:" + result);
		return result;
	}

	public static int xor(String inFile, String outFile, String psw)
			throws IOException {

		InputStream inputStream = null;
		inputStream = new FileInputStream(inFile);

		OutputStream outputStream = null;
		outputStream = new FileOutputStream(outFile);
		// prepare passwd
		int step = psw.length();
		int leng = 0;
		String totalPsw = "";
		for (leng = 0; leng < 1024; leng = leng + step) {
			totalPsw = totalPsw + psw;
		}

		byte[] totalPswB = totalPsw.getBytes();

		byte[] pwds = new byte[1024];
		for (leng = 0; leng < 1024; leng = leng + 1) {
			pwds[leng] = totalPswB[leng];
		}

		//
		int bytesWritten = 0;
		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			// xor
			for (leng = 0; leng < 1024; leng = leng + 1) {
				bytes[leng] = (byte) (bytes[leng] ^ pwds[leng]);
			}
			// write
			outputStream.write(bytes, 0, byteCount);
			bytesWritten += byteCount;
		}

		inputStream.close();
		outputStream.close();

		return 0;
	}

	/**
	 * @param args
	 *            £ºmode(+/-) file/dir
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int exec = 1;
		if (args.length != 2) {
			System.out.println("ÃüÁî¸ñÊ½´í£¡ myenc +/- file/dir");
			return;
		}
		String mode = args[0];
		String inFile = args[1];
		String pwdstr = "";

		// System.out.println(mode + inFile);

		if (!(mode.equalsIgnoreCase("+") || mode.equalsIgnoreCase("-"))) {
			System.out.println("Mode is error !");
			return;
		}

		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(System.in));
		//
		// System.out.println("ÇëÊäÈëÃÜÂë£º");
		// String pwdstr = br.readLine();

		// Console console= System.console();
		// char[] pwd = null;
		// pwd = console.readPassword("[%s]", "ÇëÊäÈëÃÜÂë£º") ;
		// System.out.println(String.valueOf(pwd));
		//
		// String pwdstr =pwd.toString();
		//
		Console cons;
		char[] passwd = null;
		if ((cons = System.console()) != null
				&& (passwd = cons.readPassword("[%s]", "ÇëÊäÈëÃÜÂë:")) != null) {

			java.util.Arrays.fill(passwd, ' ');
			pwdstr = passwd.toString();
		} else {
			System.out.println("Console error !");
			return;
		}

		if (mode.equalsIgnoreCase("+")) {
			exec = enc(inFile, pwdstr);

		} else if (mode.equalsIgnoreCase("-")) {
			exec = deEnc(inFile, pwdstr);

		} else {

			System.out.println("Mode is error !" + mode);
		}

		if (exec == 0) {
			System.out.println("³É¹¦£¡");
		} else {
			System.out.println("Ê§°Ü£¡");
		}

	}

}
