package bak;

import java.util.List;

public class Test3 {

	public static void main(String[] args) {
		String content1 = "品牌:FK 适用于各类轿车 副厂件 MF51";
		String content2 = "本田雅阁";
//		String content1 = "废杂色、混纺棉布,边角料（已分拣 ）,废棉布，废混纺布，废边角料，废布，边角料混纺";
//		String content2 = "废杂色、混纺棉布,边角料（已分拣 ）";
//		String content2 = "废杂色\\混纺'棉布'\"边角料\"（已分拣 ）";
//		String content2 = "周俊林";
//		String content2 = "废杂色混纺棉布边角料（已分拣 ）";
		
		G_name g_name1 = new G_name(content1);
		G_name g_name2 = new G_name(content2);
		LevensteinDistance ld = new LevensteinDistance();
		
		String stringContentA = g_name1.getContentString();
		String stringContentB = g_name2.getContentString();
		float stringSimilarity = ld.getDistance(stringContentA, stringContentB);
		System.out.println("stringA: " + stringContentA);
		System.out.println("stringB: " + stringContentB);
		System.out.println("string similarity: " + stringSimilarity);
		
		float listSimilarity = 0;
		List<String> contentA = g_name1.getContentList();
		List<String> contentB = g_name2.getContentList();
		for (int i = 0; i < g_name1.getSec_num(); i++) {
			for (int j = 0; j < g_name2.getSec_num(); j++) {
				String secA = contentA.get(i);
				String secB = contentB.get(j);
				float distance = ld.getDistance(secA, secB);
				System.out.println("secA_" + i + ":\t" + secA + ";\tsecB_" + j + ":\t" + secB + ";\tdis:\t"+ distance);
				listSimilarity += distance;
			}
		}
		double mod = Math.sqrt((float)g_name1.getSec_num()) + Math.sqrt((float)g_name2.getSec_num()); 
		listSimilarity /= mod;
		System.out.println("list similarity: " + listSimilarity);
		
		String filter = G_name.getFilter();
		System.out.println("filter: " + filter);
	}
}
