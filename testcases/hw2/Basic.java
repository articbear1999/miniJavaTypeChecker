class Main {
	public static void main(String[] y){

		System.out.println(new A().run());
	}
}

class A extends Main{
	public int run() {
		int x;
		int a;
		a = 1; // TE: Name error!  Variable 'y' doesn't exist.
		return x;
	}
}


