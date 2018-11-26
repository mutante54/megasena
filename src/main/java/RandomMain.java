import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMain {

	public RandomMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		Random r = new Random();

		List<Integer> g;

		for (int k = 1; k <= 21; k++) {

			g = new ArrayList<Integer>();

			System.out.println("\n Jogo [" + k + "]");

			while (g.size() < 6) {
				int dez = r.nextInt(61);
				if (dez != 0 && !g.contains(dez)) {
					System.out.print(dez + "-");
					g.add(dez);
				}
			}
		}

	}

}
