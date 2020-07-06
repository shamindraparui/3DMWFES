import java.io.*;
import java.lang.*;

class decryption
{
	public static String str=new String();
	public static void main(String []s)throws IOException
	{  
		FileInputStream fis=new FileInputStream(s[0]);  //take input string from file
		FileInputStream keystr=new FileInputStream(s[1]);
		int c,x=0,sum=0;
		String str1=new String();
		String key=new String();
		while((c=fis.read())!=-1)
			str=str+(char)c;
		while((c=keystr.read())!=-1)
		{
			key=key+(char)c;
			sum=(sum+(int)c)%256;
		}
		key=key_gen(sum,str.length());
		fis.close();
		keystr.close();
		while( x<=(str.length()%5))
		{
			str1=decryption(str,str.length(),key,s[2]);   //call encryption function
			str=str1;
			x++;
			
		}   //call encryption function
	}
	
	static String  key_gen(int key, int len)
	{
		int seed1, constant1, modulus_constant,constant2;
		String new_key=new String();
		int pseudoarray[]=new int[len];	
			
		seed1=(int)(key /2 + 0.5);
		constant1=key*key;
		constant2=(int)(constant1/2 + 0.5);
		modulus_constant=255;
		for(int i=0;i<len;i++)
		{
			seed1=(seed1*constant1+constant2) % modulus_constant;
			pseudoarray[i]=seed1;
		}
		
		for(int i=0;i<len;i++)
			new_key=new_key+(char)pseudoarray[i];			
		return new_key;
	}
	static String decryption(String str,int n,String key,String file)throws IOException
	{
		int i,j,k,r1=0,c1=0,d1=0;
		int ifxf=0,ibxf=0;
		int pos1=0,pos2=0,mid;
		int rem=n,temp,temp1=0;
		String plaintext=new String();
		String newstr=new String();
		String newstr1=new String();
		FileOutputStream fos=new FileOutputStream(file);
		if(n<=3)
		{
			for(i=1;i<=n;i++) //loop to find the optimal matrix dimensions and layer
			{
				for(j=1;j<=n;j++)
				{
					temp=(n-(i*i*j));
					if(temp<0)
					break;
					if(temp<rem)
					{
						rem=temp;
						d1=j; //no. of layers
						r1=i; //no. of rows
						c1=i; //no. of columns
					}
				}	
			
			}
		}
		
		else
		{
			for(i=2;i<=n/2;i++) //loop to find the optimal matrix dimensions and layers where layer<dimensions
			{
				for(j=1;j<=i;j++)
				{
					temp=(n-(i*i*j));
					if(temp<0)
					break;
					if(temp<(n-temp))
					{
						if(j>temp1 || j==temp1)
						{
							rem=temp;
							d1=j; //no. of layers
							r1=i; //no. of rows
							c1=i; //no. of columns
							temp1=j;
						}
					}
				
				}	
			
			}
		
		}
		if(r1==2 && d1==2)
		{
			r1=3;
			c1=3;
			d1=1;
		}
		int flag=key.length();
		if(n==9 && key.length()==8)
			key=key+" ";
		if(n==2 && key.length()==1)
			key=key+" ";
		
		int strarr[][][]=new int[d1][r1][c1]; //array for user string
		int strarr1[][][]=new int[d1][r1][c1];
		int cipharr[][][]=new int[d1][r1][c1]; //array for user string
		int keyarr[][][]=new int[d1][r1][c1]; //array for the key
		int ifxfarr[][][]=new int[d1][r1][c1]; //forward feedback x-axis array
		int ibxfarr[][][]=new int[d1][r1][c1]; //backward feedback x-axis array
		int n1=r1*c1*d1;
		int n2=n1;
		int n3=n1;
		
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					if(n1==n)
					{
						n1=0;
						n2=0;
					}
					if(n1<n && n1>=n3)
					{
						
						strarr[k][i][j]=(int)str.charAt(n1);
						cipharr[k][i][j]=(int)str.charAt(n1);
						keyarr[k][i][j]=(int)key.charAt(n2);
						ifxfarr[k][i][j]=ifxf;
						ibxfarr[k][i][j]=ibxf;
						n1++;
						n2++;
						
					}
					else if(n1<n3)
					{
						strarr[k][i][j]=(int)str.charAt(n1);
						cipharr[k][i][j]=(int)str.charAt(n1);
						keyarr[k][i][j]=(int)key.charAt(n2);
						ifxfarr[k][i][j]=ifxf;
						ibxfarr[k][i][j]=ibxf;
						n1++;
						n2++;
					}						
				}
				
			}

		}
		
		mid=(int)(r1/2);
		
		int layer[]=new int[str.length()];
		int p=0;
		for(i=0;i<r1;i++)
		{
			for(j=0;j<c1;j++)
			{
				for(k=0;k<d1;k++)
				{
					int tempo=strarr[k][i][j];
					layer[p]=tempo;
					p++;
				}
			}
		}
		int a=0;
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][i][j]=layer[a];	
					a++;
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		//z-axis--------------------------------------------------------------------------------------------------------------------
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					cipharr[k][i][j]=strarr[k][i][j];
				}
			}
		}
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j>=mid && j<(c1-1))
						{
							ifxfarr[k][i][j+1]=strarr[k][i][j];
						}
						if(j<=mid && j>0)
						{
							if(strarr[k][i][j-1]>=strarr[k][i][j])
								ifxfarr[k][i][j]=strarr[k][i][j-1]-strarr[k][i][j];
							else
								ifxfarr[k][i][j]=(256+strarr[k][i][j-1])-strarr[k][i][j];
						}
						if(j==(c1-1))
						{
							if(k==(d1-1) && i==(r1-1))
								ifxfarr[0][0][0]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ifxfarr[k+1][0][0]=strarr[k][i][j];
							else
								ifxfarr[k][i+1][0]=strarr[k][i][j];
						}
							
					}
				}
			
			}
			
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j<=mid && j>0)
						{
							ibxfarr[k][i][j-1]=strarr[k][i][j];
						}
						if(j>=mid && j<(c1-1))
						{
							if(strarr[k][i][j+1]>=strarr[k][i][j])
								ibxfarr[k][i][j]=strarr[k][i][j+1]-strarr[k][i][j];
							else
								ibxfarr[k][i][j]=(256+strarr[k][i][j+1])-strarr[k][i][j];
						}
						if(j==0)
						{
							if(k==(d1-1) && i==(r1-1))
								ibxfarr[0][0][c1-1]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ibxfarr[k+1][0][c1-1]=strarr[k][i][j];
							else
								ibxfarr[k][i+1][c1-1]=strarr[k][i][j];
						}
							
					}
				}
			}
		
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
						if((k==0 && i==0 && j==1) || (k==0 && i==0 && j==(c1-2)) || (k==0 && i==1 && j==0)
						|| (k==0 && i==1 && j==(c1-1)) || (c1==1 && d1>1 && k==1) ||(c1==1 && d1==1))
							continue;
						if(c1==2 && ((k==0 && i==0 && j==0)||(k==0 && i==1 && j==0)))
							continue;
						strarr[k][i][j]=strarr[k][i][j]-((keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256);
						if(strarr[k][i][j]<0)
							strarr[k][i][j]=strarr[k][i][j]+256;
					
					}
				}
			}
		
		
		if(c1>3)
		{
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
		
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
		
			strarr[0][0][c1-2]=strarr[0][0][c1-2]-((keyarr[0][0][c1-2]+ifxfarr[0][0][c1-2]+ibxfarr[0][0][c1-2])%256);
		
			if(strarr[0][0][c1-2]<0)
				strarr[0][0][c1-2]=strarr[0][0][c1-2]+256;
		}
		
		else if(c1==3)
		{
			
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
		}
		else if(c1==2)
		{
			ibxfarr[0][0][0]=cipharr[0][0][c1-1]-cipharr[d1-1][r1-1][0];
			
			if(ibxfarr[0][0][0]<0)
				ibxfarr[0][0][0]=ibxfarr[0][0][0]+256;
				
			ifxfarr[0][1][0]=ibxfarr[0][0][0];
			
			strarr[0][0][0]=strarr[0][0][0]-((keyarr[0][0][0]+ifxfarr[0][0][0]+ibxfarr[0][0][0])%256);
			
			if(strarr[0][0][0]<0)
				strarr[0][0][0]=strarr[0][0][0]+256;
				
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
			
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
				
		}
		else if(c1==1)
		{
			if(d1==1)
			{
				strarr[0][0][0]=strarr[0][0][0]-(4*keyarr[0][0][0])%256;
			
				if(strarr[0][0][0]<0)
					strarr[0][0][0]=strarr[0][0][0]+512;
				
				strarr[0][0][0]=strarr[0][0][0]/4;
			
				ifxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0])%256;
				ibxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0]+ifxfarr[0][0][0])%256;
			}
			
			else
			{
				ifxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				ibxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				strarr[1][0][0]=strarr[1][0][0]-((keyarr[1][0][0]+ifxfarr[1][0][0]+ibxfarr[1][0][0])%256);
				
				if(strarr[1][0][0]<0)
				strarr[1][0][0]=strarr[1][0][0]+256;
			
			}
			
		}
		
		if(c1>1)
		{
			ifxfarr[0][1][0]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1]+ifxfarr[0][0][c1-1])%256;
		
			strarr[0][1][0]=strarr[0][1][0]-((keyarr[0][1][0]+ifxfarr[0][1][0]+ibxfarr[0][1][0])%256);
		
			if(strarr[0][1][0]<0)
				strarr[0][1][0]=strarr[0][1][0]+256;
			
			ibxfarr[0][1][c1-1]=(strarr[0][0][0]+keyarr[0][0][0]+ibxfarr[0][0][0])%256;
		
			strarr[0][1][c1-1]=strarr[0][1][c1-1]-((keyarr[0][1][c1-1]+ifxfarr[0][1][c1-1]+ibxfarr[0][1][c1-1])%256);
		
			if(strarr[0][1][c1-1]<0)
				strarr[0][1][c1-1]=strarr[0][1][c1-1]+256;
		
		}	
		 
		int layer1[]=new int[str.length()];
		int p1=0;
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					int tempo1=strarr[k][i][j];
					layer1[p1]=tempo1;
					p1++;
				}
			}
		}
		int q=0;
		for(i=0;i<r1;i++)   
		{
			for(j=0;j<c1;j++)
			{
				for(k=0;k<d1;k++)
				{
					strarr1[k][i][j]=layer1[q];	
					q++;
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][j][i]=strarr[k][i][j];					
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		//y-axis--------------------------------------------------------------------------------------------------------------------
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					cipharr[k][i][j]=strarr[k][i][j];
				}
			}
		}
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j>=mid && j<(c1-1))
						{
							ifxfarr[k][i][j+1]=strarr[k][i][j];
						}
						if(j<=mid && j>0)
						{
							if(strarr[k][i][j-1]>=strarr[k][i][j])
								ifxfarr[k][i][j]=strarr[k][i][j-1]-strarr[k][i][j];
							else
								ifxfarr[k][i][j]=(256+strarr[k][i][j-1])-strarr[k][i][j];
						}
						if(j==(c1-1))
						{
							if(k==(d1-1) && i==(r1-1))
								ifxfarr[0][0][0]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ifxfarr[k+1][0][0]=strarr[k][i][j];
							else
								ifxfarr[k][i+1][0]=strarr[k][i][j];
						}
							
					}
				}
			
			}
			
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j<=mid && j>0)
						{
							ibxfarr[k][i][j-1]=strarr[k][i][j];
						}
						if(j>=mid && j<(c1-1))
						{
							if(strarr[k][i][j+1]>=strarr[k][i][j])
								ibxfarr[k][i][j]=strarr[k][i][j+1]-strarr[k][i][j];
							else
								ibxfarr[k][i][j]=(256+strarr[k][i][j+1])-strarr[k][i][j];
						}
						if(j==0)
						{
							if(k==(d1-1) && i==(r1-1))
								ibxfarr[0][0][c1-1]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ibxfarr[k+1][0][c1-1]=strarr[k][i][j];
							else
								ibxfarr[k][i+1][c1-1]=strarr[k][i][j];
						}
							
					}
				}
			}
		
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
						if((k==0 && i==0 && j==1) || (k==0 && i==0 && j==(c1-2)) || (k==0 && i==1 && j==0)
						|| (k==0 && i==1 && j==(c1-1)) || (c1==1 && d1>1 && k==1) ||(c1==1 && d1==1))
							continue;
						if(c1==2 && ((k==0 && i==0 && j==0)||(k==0 && i==1 && j==0)))
							continue;
						strarr[k][i][j]=strarr[k][i][j]-((keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256);
						if(strarr[k][i][j]<0)
							strarr[k][i][j]=strarr[k][i][j]+256;
					
					}
				}
			}
		
		
		if(c1>3)
		{
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
		
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
		
			strarr[0][0][c1-2]=strarr[0][0][c1-2]-((keyarr[0][0][c1-2]+ifxfarr[0][0][c1-2]+ibxfarr[0][0][c1-2])%256);
		
			if(strarr[0][0][c1-2]<0)
				strarr[0][0][c1-2]=strarr[0][0][c1-2]+256;
		}
		
		else if(c1==3)
		{
			
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
		}
		else if(c1==2)
		{
			ibxfarr[0][0][0]=cipharr[0][0][c1-1]-cipharr[d1-1][r1-1][0];
			
			if(ibxfarr[0][0][0]<0)
				ibxfarr[0][0][0]=ibxfarr[0][0][0]+256;
				
			ifxfarr[0][1][0]=ibxfarr[0][0][0];
			
			strarr[0][0][0]=strarr[0][0][0]-((keyarr[0][0][0]+ifxfarr[0][0][0]+ibxfarr[0][0][0])%256);
			
			if(strarr[0][0][0]<0)
				strarr[0][0][0]=strarr[0][0][0]+256;
				
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
			
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
				
		}
		else if(c1==1)
		{
			if(d1==1)
			{
				strarr[0][0][0]=strarr[0][0][0]-(4*keyarr[0][0][0])%256;
			
				if(strarr[0][0][0]<0)
					strarr[0][0][0]=strarr[0][0][0]+512;
				
				strarr[0][0][0]=strarr[0][0][0]/4;
			
				ifxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0])%256;
				ibxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0]+ifxfarr[0][0][0])%256;
			}
			
			else
			{
				ifxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				ibxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				strarr[1][0][0]=strarr[1][0][0]-((keyarr[1][0][0]+ifxfarr[1][0][0]+ibxfarr[1][0][0])%256);
				
				if(strarr[1][0][0]<0)
				strarr[1][0][0]=strarr[1][0][0]+256;
			
			}
			
		}
		
		if(c1>1)
		{
			ifxfarr[0][1][0]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1]+ifxfarr[0][0][c1-1])%256;
		
			strarr[0][1][0]=strarr[0][1][0]-((keyarr[0][1][0]+ifxfarr[0][1][0]+ibxfarr[0][1][0])%256);
		
			if(strarr[0][1][0]<0)
				strarr[0][1][0]=strarr[0][1][0]+256;
			
			ibxfarr[0][1][c1-1]=(strarr[0][0][0]+keyarr[0][0][0]+ibxfarr[0][0][0])%256;
		
			strarr[0][1][c1-1]=strarr[0][1][c1-1]-((keyarr[0][1][c1-1]+ifxfarr[0][1][c1-1]+ibxfarr[0][1][c1-1])%256);
		
			if(strarr[0][1][c1-1]<0)
				strarr[0][1][c1-1]=strarr[0][1][c1-1]+256;
		
		}	
		
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][i][j]=strarr[k][j][i];					
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		//x-axis--------------------------------------------------------------------------------------------------------------------
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					cipharr[k][i][j]=strarr[k][i][j];
				}
			}
		}
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j>=mid && j<(c1-1))
						{
							ifxfarr[k][i][j+1]=strarr[k][i][j];
						}
						if(j<=mid && j>0)
						{
							if(strarr[k][i][j-1]>=strarr[k][i][j])
								ifxfarr[k][i][j]=strarr[k][i][j-1]-strarr[k][i][j];
							else
								ifxfarr[k][i][j]=(256+strarr[k][i][j-1])-strarr[k][i][j];
						}
						if(j==(c1-1))
						{
							if(k==(d1-1) && i==(r1-1))
								ifxfarr[0][0][0]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ifxfarr[k+1][0][0]=strarr[k][i][j];
							else
								ifxfarr[k][i+1][0]=strarr[k][i][j];
						}
							
					}
				}
			
			}
			
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j<=mid && j>0)
						{
							ibxfarr[k][i][j-1]=strarr[k][i][j];
						}
						if(j>=mid && j<(c1-1))
						{
							if(strarr[k][i][j+1]>=strarr[k][i][j])
								ibxfarr[k][i][j]=strarr[k][i][j+1]-strarr[k][i][j];
							else
								ibxfarr[k][i][j]=(256+strarr[k][i][j+1])-strarr[k][i][j];
						}
						if(j==0)
						{
							if(k==(d1-1) && i==(r1-1))
								ibxfarr[0][0][c1-1]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ibxfarr[k+1][0][c1-1]=strarr[k][i][j];
							else
								ibxfarr[k][i+1][c1-1]=strarr[k][i][j];
						}
							
					}
				}
			}
		
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
						if((k==0 && i==0 && j==1) || (k==0 && i==0 && j==(c1-2)) || (k==0 && i==1 && j==0)
						|| (k==0 && i==1 && j==(c1-1)) || (c1==1 && d1>1 && k==1) ||(c1==1 && d1==1))
							continue;
						if(c1==2 && ((k==0 && i==0 && j==0)||(k==0 && i==1 && j==0)))
							continue;
						strarr[k][i][j]=strarr[k][i][j]-((keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256);
						if(strarr[k][i][j]<0)
							strarr[k][i][j]=strarr[k][i][j]+256;
					
					}
				}
			}
		
		
		if(c1>3)
		{
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
		
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
		
			strarr[0][0][c1-2]=strarr[0][0][c1-2]-((keyarr[0][0][c1-2]+ifxfarr[0][0][c1-2]+ibxfarr[0][0][c1-2])%256);
		
			if(strarr[0][0][c1-2]<0)
				strarr[0][0][c1-2]=strarr[0][0][c1-2]+256;
		}
		
		else if(c1==3)
		{
			
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
		}
		else if(c1==2)
		{
			ibxfarr[0][0][0]=cipharr[0][0][c1-1]-cipharr[d1-1][r1-1][0];
			
			if(ibxfarr[0][0][0]<0)
				ibxfarr[0][0][0]=ibxfarr[0][0][0]+256;
				
			ifxfarr[0][1][0]=ibxfarr[0][0][0];
			
			strarr[0][0][0]=strarr[0][0][0]-((keyarr[0][0][0]+ifxfarr[0][0][0]+ibxfarr[0][0][0])%256);
			
			if(strarr[0][0][0]<0)
				strarr[0][0][0]=strarr[0][0][0]+256;
				
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
			
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
				
		}
		else if(c1==1)
		{
			if(d1==1)
			{
				strarr[0][0][0]=strarr[0][0][0]-(4*keyarr[0][0][0])%256;
			
				if(strarr[0][0][0]<0)
					strarr[0][0][0]=strarr[0][0][0]+512;
				
				strarr[0][0][0]=strarr[0][0][0]/4;
			
				ifxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0])%256;
				ibxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0]+ifxfarr[0][0][0])%256;
			}
			
			else
			{
				ifxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				ibxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				strarr[1][0][0]=strarr[1][0][0]-((keyarr[1][0][0]+ifxfarr[1][0][0]+ibxfarr[1][0][0])%256);
				
				if(strarr[1][0][0]<0)
				strarr[1][0][0]=strarr[1][0][0]+256;
			
			}
			
		}
		
		if(c1>1)
		{
			ifxfarr[0][1][0]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1]+ifxfarr[0][0][c1-1])%256;
		
			strarr[0][1][0]=strarr[0][1][0]-((keyarr[0][1][0]+ifxfarr[0][1][0]+ibxfarr[0][1][0])%256);
		
			if(strarr[0][1][0]<0)
				strarr[0][1][0]=strarr[0][1][0]+256;
			
			ibxfarr[0][1][c1-1]=(strarr[0][0][0]+keyarr[0][0][0]+ibxfarr[0][0][0])%256;
		
			strarr[0][1][c1-1]=strarr[0][1][c1-1]-((keyarr[0][1][c1-1]+ifxfarr[0][1][c1-1]+ibxfarr[0][1][c1-1])%256);
		
			if(strarr[0][1][c1-1]<0)
				strarr[0][1][c1-1]=strarr[0][1][c1-1]+256;
		
		}	
		
		
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					newstr=newstr+(char)strarr[k][i][j];
				}
			}
		}
		n1=r1*c1*d1;
		n2=n-n1;
		n3=n1;
		i=n2;
		while(i<n1)
		{
			newstr1=newstr1+newstr.charAt(i);
			i++;
		}
		i=n1-n2;
		while(i<n1)
		{
			newstr1=newstr1+str.charAt(i);
			i++;
		}
		i=0;
		while(i<n2)
		{				
			newstr1=newstr1+newstr.charAt(i);
			i++;
		}
		
		//-------------------------------------------------------------------------------------------------------------------------------------
		
		//-------------------------------------------------------------------------------------------------------------------------------------
		if(n==1)
			fos.write(strarr[0][0][0]);
	
	else{
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=(int)newstr1.charAt(pos1);
					cipharr[k][i][j]=(int)newstr1.charAt(pos1);
					keyarr[k][i][j]=(int)key.charAt(pos2);
					ifxfarr[k][i][j]=ifxf;
					ibxfarr[k][i][j]=ibxf;
					pos1++;
					pos2++;
				}
			}
		}
		mid=(int)(r1/2);
		
		int layer2[]=new int[str.length()];
		int p2=0;
		for(i=0;i<r1;i++)
		{
			for(j=0;j<c1;j++)
			{
				for(k=0;k<d1;k++)
				{
					int tempo=strarr[k][i][j];
					layer2[p2]=tempo;
					p2++;
				}
			}
		}
		int a1=0;
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][i][j]=layer2[a1];	
					a1++;
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		//z-axis--------------------------------------------------------------------------------------------------------------------
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					cipharr[k][i][j]=strarr[k][i][j];
				}
			}
		}
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j>=mid && j<(c1-1))
						{
							ifxfarr[k][i][j+1]=strarr[k][i][j];
						}
						if(j<=mid && j>0)
						{
							if(strarr[k][i][j-1]>=strarr[k][i][j])
								ifxfarr[k][i][j]=strarr[k][i][j-1]-strarr[k][i][j];
							else
								ifxfarr[k][i][j]=(256+strarr[k][i][j-1])-strarr[k][i][j];
						}
						if(j==(c1-1))
						{
							if(k==(d1-1) && i==(r1-1))
								ifxfarr[0][0][0]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ifxfarr[k+1][0][0]=strarr[k][i][j];
							else
								ifxfarr[k][i+1][0]=strarr[k][i][j];
						}
							
					}
				}
			
			}
			
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j<=mid && j>0)
						{
							ibxfarr[k][i][j-1]=strarr[k][i][j];
						}
						if(j>=mid && j<(c1-1))
						{
							if(strarr[k][i][j+1]>=strarr[k][i][j])
								ibxfarr[k][i][j]=strarr[k][i][j+1]-strarr[k][i][j];
							else
								ibxfarr[k][i][j]=(256+strarr[k][i][j+1])-strarr[k][i][j];
						}
						if(j==0)
						{
							if(k==(d1-1) && i==(r1-1))
								ibxfarr[0][0][c1-1]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ibxfarr[k+1][0][c1-1]=strarr[k][i][j];
							else
								ibxfarr[k][i+1][c1-1]=strarr[k][i][j];
						}
							
					}
				}
			}
		
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
						if((k==0 && i==0 && j==1) || (k==0 && i==0 && j==(c1-2)) || (k==0 && i==1 && j==0)
						|| (k==0 && i==1 && j==(c1-1)) || (c1==1 && d1>1 && k==1) ||(c1==1 && d1==1))
							continue;
						if(c1==2 && ((k==0 && i==0 && j==0)||(k==0 && i==1 && j==0)))
							continue;
						strarr[k][i][j]=strarr[k][i][j]-((keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256);
						if(strarr[k][i][j]<0)
							strarr[k][i][j]=strarr[k][i][j]+256;
					
					}
				}
			}
		
		
		if(c1>3)
		{
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
		
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
		
			strarr[0][0][c1-2]=strarr[0][0][c1-2]-((keyarr[0][0][c1-2]+ifxfarr[0][0][c1-2]+ibxfarr[0][0][c1-2])%256);
		
			if(strarr[0][0][c1-2]<0)
				strarr[0][0][c1-2]=strarr[0][0][c1-2]+256;
		}
		
		else if(c1==3)
		{
			
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
		}
		else if(c1==2)
		{
			ibxfarr[0][0][0]=cipharr[0][0][c1-1]-cipharr[d1-1][r1-1][0];
			
			if(ibxfarr[0][0][0]<0)
				ibxfarr[0][0][0]=ibxfarr[0][0][0]+256;
				
			ifxfarr[0][1][0]=ibxfarr[0][0][0];
			
			strarr[0][0][0]=strarr[0][0][0]-((keyarr[0][0][0]+ifxfarr[0][0][0]+ibxfarr[0][0][0])%256);
			
			if(strarr[0][0][0]<0)
				strarr[0][0][0]=strarr[0][0][0]+256;
				
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
			
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
				
		}
		else if(c1==1)
		{
			if(d1==1)
			{
				strarr[0][0][0]=strarr[0][0][0]-(4*keyarr[0][0][0])%256;
			
				if(strarr[0][0][0]<0)
					strarr[0][0][0]=strarr[0][0][0]+512;
				
				strarr[0][0][0]=strarr[0][0][0]/4;
			
				ifxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0])%256;
				ibxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0]+ifxfarr[0][0][0])%256;
			}
			
			else
			{
				ifxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				ibxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				strarr[1][0][0]=strarr[1][0][0]-((keyarr[1][0][0]+ifxfarr[1][0][0]+ibxfarr[1][0][0])%256);
				
				if(strarr[1][0][0]<0)
				strarr[1][0][0]=strarr[1][0][0]+256;
			
			}
			
		}
		
		if(c1>1)
		{
			ifxfarr[0][1][0]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1]+ifxfarr[0][0][c1-1])%256;
		
			strarr[0][1][0]=strarr[0][1][0]-((keyarr[0][1][0]+ifxfarr[0][1][0]+ibxfarr[0][1][0])%256);
		
			if(strarr[0][1][0]<0)
				strarr[0][1][0]=strarr[0][1][0]+256;
			
			ibxfarr[0][1][c1-1]=(strarr[0][0][0]+keyarr[0][0][0]+ibxfarr[0][0][0])%256;
		
			strarr[0][1][c1-1]=strarr[0][1][c1-1]-((keyarr[0][1][c1-1]+ifxfarr[0][1][c1-1]+ibxfarr[0][1][c1-1])%256);
		
			if(strarr[0][1][c1-1]<0)
				strarr[0][1][c1-1]=strarr[0][1][c1-1]+256;
		
		}	
		int layer3[]=new int[str.length()];
		int p3=0;
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					int tempo1=strarr[k][i][j];
					layer3[p3]=tempo1;
					p3++;
				}
			}
		}
		int q1=0;
		for(i=0;i<r1;i++)   
		{
			for(j=0;j<c1;j++)
			{
				for(k=0;k<d1;k++)
				{
					strarr1[k][i][j]=layer3[q1];	
					q1++;
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][j][i]=strarr[k][i][j];					
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		//y-axis--------------------------------------------------------------------------------------------------------------------
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					cipharr[k][i][j]=strarr[k][i][j];
				}
			}
		}
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j>=mid && j<(c1-1))
						{
							ifxfarr[k][i][j+1]=strarr[k][i][j];
						}
						if(j<=mid && j>0)
						{
							if(strarr[k][i][j-1]>=strarr[k][i][j])
								ifxfarr[k][i][j]=strarr[k][i][j-1]-strarr[k][i][j];
							else
								ifxfarr[k][i][j]=(256+strarr[k][i][j-1])-strarr[k][i][j];
						}
						if(j==(c1-1))
						{
							if(k==(d1-1) && i==(r1-1))
								ifxfarr[0][0][0]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ifxfarr[k+1][0][0]=strarr[k][i][j];
							else
								ifxfarr[k][i+1][0]=strarr[k][i][j];
						}
							
					}
				}
			
			}
			
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j<=mid && j>0)
						{
							ibxfarr[k][i][j-1]=strarr[k][i][j];
						}
						if(j>=mid && j<(c1-1))
						{
							if(strarr[k][i][j+1]>=strarr[k][i][j])
								ibxfarr[k][i][j]=strarr[k][i][j+1]-strarr[k][i][j];
							else
								ibxfarr[k][i][j]=(256+strarr[k][i][j+1])-strarr[k][i][j];
						}
						if(j==0)
						{
							if(k==(d1-1) && i==(r1-1))
								ibxfarr[0][0][c1-1]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ibxfarr[k+1][0][c1-1]=strarr[k][i][j];
							else
								ibxfarr[k][i+1][c1-1]=strarr[k][i][j];
						}
							
					}
				}
			}
		
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
						if((k==0 && i==0 && j==1) || (k==0 && i==0 && j==(c1-2)) || (k==0 && i==1 && j==0)
						|| (k==0 && i==1 && j==(c1-1)) || (c1==1 && d1>1 && k==1) ||(c1==1 && d1==1))
							continue;
						if(c1==2 && ((k==0 && i==0 && j==0)||(k==0 && i==1 && j==0)))
							continue;
						strarr[k][i][j]=strarr[k][i][j]-((keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256);
						if(strarr[k][i][j]<0)
							strarr[k][i][j]=strarr[k][i][j]+256;
					
					}
				}
			}
		
		
		if(c1>3)
		{
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
		
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
		
			strarr[0][0][c1-2]=strarr[0][0][c1-2]-((keyarr[0][0][c1-2]+ifxfarr[0][0][c1-2]+ibxfarr[0][0][c1-2])%256);
		
			if(strarr[0][0][c1-2]<0)
				strarr[0][0][c1-2]=strarr[0][0][c1-2]+256;
		}
		
		else if(c1==3)
		{
			
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
		}
		else if(c1==2)
		{
			ibxfarr[0][0][0]=cipharr[0][0][c1-1]-cipharr[d1-1][r1-1][0];
			
			if(ibxfarr[0][0][0]<0)
				ibxfarr[0][0][0]=ibxfarr[0][0][0]+256;
				
			ifxfarr[0][1][0]=ibxfarr[0][0][0];
			
			strarr[0][0][0]=strarr[0][0][0]-((keyarr[0][0][0]+ifxfarr[0][0][0]+ibxfarr[0][0][0])%256);
			
			if(strarr[0][0][0]<0)
				strarr[0][0][0]=strarr[0][0][0]+256;
				
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
			
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
				
		}
		else if(c1==1)
		{
			if(d1==1)
			{
				strarr[0][0][0]=strarr[0][0][0]-(4*keyarr[0][0][0])%256;
			
				if(strarr[0][0][0]<0)
					strarr[0][0][0]=strarr[0][0][0]+512;
				
				strarr[0][0][0]=strarr[0][0][0]/4;
			
				ifxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0])%256;
				ibxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0]+ifxfarr[0][0][0])%256;
			}
			
			else
			{
				ifxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				ibxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				strarr[1][0][0]=strarr[1][0][0]-((keyarr[1][0][0]+ifxfarr[1][0][0]+ibxfarr[1][0][0])%256);
				
				if(strarr[1][0][0]<0)
				strarr[1][0][0]=strarr[1][0][0]+256;
			
			}
			
		}
		
		if(c1>1)
		{
			ifxfarr[0][1][0]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1]+ifxfarr[0][0][c1-1])%256;
		
			strarr[0][1][0]=strarr[0][1][0]-((keyarr[0][1][0]+ifxfarr[0][1][0]+ibxfarr[0][1][0])%256);
		
			if(strarr[0][1][0]<0)
				strarr[0][1][0]=strarr[0][1][0]+256;
			
			ibxfarr[0][1][c1-1]=(strarr[0][0][0]+keyarr[0][0][0]+ibxfarr[0][0][0])%256;
		
			strarr[0][1][c1-1]=strarr[0][1][c1-1]-((keyarr[0][1][c1-1]+ifxfarr[0][1][c1-1]+ibxfarr[0][1][c1-1])%256);
		
			if(strarr[0][1][c1-1]<0)
				strarr[0][1][c1-1]=strarr[0][1][c1-1]+256;
		
		}	
		
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][i][j]=strarr[k][j][i];					
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		//x-axis--------------------------------------------------------------------------------------------------------------------
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					cipharr[k][i][j]=strarr[k][i][j];
				}
			}
		}
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j>=mid && j<(c1-1))
						{
							ifxfarr[k][i][j+1]=strarr[k][i][j];
						}
						if(j<=mid && j>0)
						{
							if(strarr[k][i][j-1]>=strarr[k][i][j])
								ifxfarr[k][i][j]=strarr[k][i][j-1]-strarr[k][i][j];
							else
								ifxfarr[k][i][j]=(256+strarr[k][i][j-1])-strarr[k][i][j];
						}
						if(j==(c1-1))
						{
							if(k==(d1-1) && i==(r1-1))
								ifxfarr[0][0][0]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ifxfarr[k+1][0][0]=strarr[k][i][j];
							else
								ifxfarr[k][i+1][0]=strarr[k][i][j];
						}
							
					}
				}
			
			}
			
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
					
						if(j<=mid && j>0)
						{
							ibxfarr[k][i][j-1]=strarr[k][i][j];
						}
						if(j>=mid && j<(c1-1))
						{
							if(strarr[k][i][j+1]>=strarr[k][i][j])
								ibxfarr[k][i][j]=strarr[k][i][j+1]-strarr[k][i][j];
							else
								ibxfarr[k][i][j]=(256+strarr[k][i][j+1])-strarr[k][i][j];
						}
						if(j==0)
						{
							if(k==(d1-1) && i==(r1-1))
								ibxfarr[0][0][c1-1]=strarr[k][i][j];
							else if(i==(r1-1) && k<(d1-1))
								ibxfarr[k+1][0][c1-1]=strarr[k][i][j];
							else
								ibxfarr[k][i+1][c1-1]=strarr[k][i][j];
						}
							
					}
				}
			}
		
			for(k=0;k<d1;k++)
			{
				for(i=0;i<r1;i++)
				{
					for(j=0;j<c1;j++)
					{
						if((k==0 && i==0 && j==1) || (k==0 && i==0 && j==(c1-2)) || (k==0 && i==1 && j==0)
						|| (k==0 && i==1 && j==(c1-1)) || (c1==1 && d1>1 && k==1) ||(c1==1 && d1==1))
							continue;
						if(c1==2 && ((k==0 && i==0 && j==0)||(k==0 && i==1 && j==0)))
							continue;
						strarr[k][i][j]=strarr[k][i][j]-((keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256);
						if(strarr[k][i][j]<0)
							strarr[k][i][j]=strarr[k][i][j]+256;
					
					}
				}
			}
		
		
		if(c1>3)
		{
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
		
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
		
			strarr[0][0][c1-2]=strarr[0][0][c1-2]-((keyarr[0][0][c1-2]+ifxfarr[0][0][c1-2]+ibxfarr[0][0][c1-2])%256);
		
			if(strarr[0][0][c1-2]<0)
				strarr[0][0][c1-2]=strarr[0][0][c1-2]+256;
		}
		
		else if(c1==3)
		{
			
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			ibxfarr[0][0][c1-2]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
		
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
			
		}
		else if(c1==2)
		{
			ibxfarr[0][0][0]=cipharr[0][0][c1-1]-cipharr[d1-1][r1-1][0];
			
			if(ibxfarr[0][0][0]<0)
				ibxfarr[0][0][0]=ibxfarr[0][0][0]+256;
				
			ifxfarr[0][1][0]=ibxfarr[0][0][0];
			
			strarr[0][0][0]=strarr[0][0][0]-((keyarr[0][0][0]+ifxfarr[0][0][0]+ibxfarr[0][0][0])%256);
			
			if(strarr[0][0][0]<0)
				strarr[0][0][0]=strarr[0][0][0]+256;
				
			ifxfarr[0][0][1]=(strarr[0][0][0]+keyarr[0][0][0])%256;
			
			strarr[0][0][1]=strarr[0][0][1]-((keyarr[0][0][1]+ifxfarr[0][0][1]+ibxfarr[0][0][1])%256);
			
			if(strarr[0][0][1]<0)
				strarr[0][0][1]=strarr[0][0][1]+256;
				
		}
		else if(c1==1)
		{
			if(d1==1)
			{
				strarr[0][0][0]=strarr[0][0][0]-(4*keyarr[0][0][0])%256;
			
				if(strarr[0][0][0]<0)
					strarr[0][0][0]=strarr[0][0][0]+512;
				
				strarr[0][0][0]=strarr[0][0][0]/4;
			
				ifxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0])%256;
				ibxfarr[0][0][0]=(strarr[0][0][0]+keyarr[0][0][0]+ifxfarr[0][0][0])%256;
			}
			
			else
			{
				ifxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				ibxfarr[1][0][0]=strarr[0][0][0]+keyarr[0][0][0];
				
				strarr[1][0][0]=strarr[1][0][0]-((keyarr[1][0][0]+ifxfarr[1][0][0]+ibxfarr[1][0][0])%256);
				
				if(strarr[1][0][0]<0)
				strarr[1][0][0]=strarr[1][0][0]+256;
			
			}
			
		}
		
		if(c1>1)
		{
			ifxfarr[0][1][0]=(strarr[0][0][c1-1]+keyarr[0][0][c1-1]+ifxfarr[0][0][c1-1])%256;
		
			strarr[0][1][0]=strarr[0][1][0]-((keyarr[0][1][0]+ifxfarr[0][1][0]+ibxfarr[0][1][0])%256);
		
			if(strarr[0][1][0]<0)
				strarr[0][1][0]=strarr[0][1][0]+256;
			
			ibxfarr[0][1][c1-1]=(strarr[0][0][0]+keyarr[0][0][0]+ibxfarr[0][0][0])%256;
		
			strarr[0][1][c1-1]=strarr[0][1][c1-1]-((keyarr[0][1][c1-1]+ifxfarr[0][1][c1-1]+ibxfarr[0][1][c1-1])%256);
		
			if(strarr[0][1][c1-1]<0)
				strarr[0][1][c1-1]=strarr[0][1][c1-1]+256;
		
		}	
		
		
		
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					plaintext=plaintext+(char)strarr[k][i][j];
					
				}
			}
		}
		n1=r1*c1*d1;
		i=n1;
		while(i<n)
		{
			plaintext=plaintext+newstr1.charAt(i);
			i++;
		}
		if(flag==8 || flag==1)
			n=n-1;
		for(i=0;i<n;i++)
			fos.write(plaintext.charAt(i));
		str=plaintext;
		
		fos.close();}
		return str;
	
	}	
		
}