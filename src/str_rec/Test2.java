package str_rec;

public class Test2 {

	public static void main(String[] args) {
		float[][] temp = new float[10][10];
		
		for (int i = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++) {
				temp[i][j] = i + j / 10.0f;
			}
		}
		
		for (int j = 0; j < 10; j++) {
        	for (int i = j + 1; i < 10; i++) {
        		temp[i][j] = temp[j][i];
        	}
        }
		
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(temp[i][j] + " ");
			}
			System.out.println();
		}
	}
}
