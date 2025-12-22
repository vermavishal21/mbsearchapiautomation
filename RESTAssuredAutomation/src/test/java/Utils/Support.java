package Utils;

public class Support {
	
	
	public static double convertPriceD(String priceD) {

	    priceD = priceD.replace("₹", "")
	                   .replace(",", "")
	                   .trim();

	    if (priceD.contains("Cr")) {
	        return Double.parseDouble(priceD.replace("Cr", "").trim()) * 10000000;
	    }

	    if (priceD.contains("Lac") || priceD.contains("Lakh")) {
	        return Double.parseDouble(priceD.replaceAll("Lac|Lakh", "").trim()) * 100000;
	    }

	    if (priceD.contains("K")) {
	        return Double.parseDouble(priceD.replace("K", "").trim()) * 1000;
	    }

	    return Double.parseDouble(priceD);
	}

}
