package kr.co.pulmuone.v1.comm.util;

public class PriceUtil
{
	static int discountPrice = 0;

	//정률
	public static int getPriceByRate(int salePrice, int discountRate)
	{

		discountPrice = salePrice - (int) Math.ceil(salePrice * discountRate / 100);

		return discountPrice;
	}


	//고정가
	public static int getPriceByPrice(int salePrice, int discountRate)
	{

		discountPrice = salePrice - discountRate;
		if(discountPrice < 0) {
			discountPrice = 0 ;
		}
		return discountPrice;
	}

	//할인율
	public static int getRate(int recommendedPrice, int salePrice) {

		int discountRate = (int)Math.floor((double)(recommendedPrice - salePrice) / (double)recommendedPrice *100) ;

		return discountRate;
	}

	//골라담기 단가
	public static int getSelectPrice(int price, int count){
		return (int) (Math.round(Math.ceil((float) price / count / 10)) * 10);
	}
}
