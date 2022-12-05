
public class Predictor6400 extends Predictor {

	Table pag = new Table(2048,2);
	Table ghr = new Table(128,11);
	
	public Predictor6400() {
	for(int i=0;i<=2047;i++)
	{
		for(int j=0;j<=1;j++)
		{
			pag.setBit(i,j,false);
		}
	}
	for(int i=0;i<=127;i++)
	{
		for(int j=0;j<=10;j++)
		{
			ghr.setBit(i,j,false);
		}
	}
		
	}


	public void Train(long address, boolean outcome, boolean predict) {

		boolean value1;
		int address_value=0;
		int access_ghr=0;
		boolean zero_bit;
		boolean first_bit;
		for(int i=11;i<=17;i++)
		{		
			if((address & (1<<i))!=0)
				access_ghr=access_ghr+(int)(1*Math.pow(2,11-i));
		}

		for(int i=10;i>=0;i--)
		{
			value1=ghr.getBit(access_ghr,i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,10-i));
		}


		zero_bit=pag.getBit(address_value,1);
		first_bit=pag.getBit(address_value,0);

		if(zero_bit==false && first_bit==false)
		{
			if(outcome==true)
			{
				pag.setBit(address_value,1,true);
			}				

		}
		else if(first_bit==false && zero_bit==true)
		{
			if(outcome==true)
			{
				pag.setBit(address_value,1,false);
				pag.setBit(address_value,0,true);
			}
			else
			{
				pag.setBit(address_value,1,false);
			}
		}
		else if(first_bit==true && zero_bit==false)
		{
     			if(outcome==true)
			{
				pag.setBit(address_value,1,true);
			}
			else
			{
				pag.setBit(address_value,1,true);
				pag.setBit(address_value,0,false);
			}
		}
		else if(first_bit==true && zero_bit==true)
		{
       			if(outcome==false)
			{
				pag.setBit(address_value,1,false);
			}
				
		}

		for(int i=9;i>=0;i--)
		{
			value1=ghr.getBit(access_ghr,i);
			ghr.setBit(access_ghr,i+1,value1);
		}
		if(outcome==true)
			ghr.setBit(access_ghr,0,true);	
		else
			ghr.setBit(access_ghr,0,false);

		
	}


	public boolean predict(long address){
		boolean value1;
		int address_value=0;
		int access_ghr=0;
		boolean zero_bit;
		boolean first_bit;
		for(int i=11;i<=17;i++)
		{		
			if((address & (1<<i))!=0)
				access_ghr=access_ghr+(int)(1*Math.pow(2,11-i));
		}

		for(int i=10;i>=0;i--)
		{
			value1=ghr.getBit(access_ghr,i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,10-i));
		}


		zero_bit=pag.getBit(address_value,1);
		first_bit=pag.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			return false;
		else if(first_bit==false && zero_bit==true)
			return false;
		else if(first_bit==true && zero_bit==false)
			return true;
		else if(first_bit==true && zero_bit==true)
			return true;
		return false;
		
	}

}
