import java.io.*;
import java.lang.*;
import java.util.Timer;

class encryption
{
	static int count=0;
	public static String str=new String();
	public static void main(String []s)throws IOException
	{  
		long startTime=System.nanoTime();
		FileInputStream usrstr=new FileInputStream(s[0]);  //take input string from user
		FileInputStream keystr=new FileInputStream(s[1]); //take the key as input 
		
		String str1=new String();
		String key=new String();
		int c,sum=0;
		while((c=usrstr.read())!=-1)
			str=str+(char)c;
		while((c=keystr.read())!=-1)
		{
			key=key+(char)c;
			sum=(sum+(int)c)%256;
		}
		if(str.length()==8 || str.length()==1)
		{
			str=str+" ";
		}
		key=key_gen(sum,str.length());
		int x=0;
		while( x<=(str.length()%5))
		{
			str1=encryption(str,str.length(),key,s[2]);   //call encryption function
			str=str1;
			x++;
			
		}
		System.out.println(count);
		long endTime=System.nanoTime();
		long duration=endTime-startTime;
		System.out.println((duration/1000000)+ " ms");
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
	static String encryption(String str,int n,String key,String file)throws IOException
	{
		int i,j,k,r1=0,c1=0,d1=0;
		int ifxf=0,ibxf=0,ifyf=0,ibyf=0,ifzf=0,ibzf=0;
		FileOutputStream fos=new FileOutputStream(file);
		String midstr=new String();
		String initstr=new String();
		String lststr=new String();
		String ciphstr=new String();
		String newstr=new String();
		String newstr1=new String();
		int rem=n,temp,temp1,c;
		temp1=0;
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
		if(n==8)
		{
			str=str+" ";
			key=key+" ";
			n=9;
		}
		if(n==1)
		{
			str=str+" ";
			key=key+" ";
			n=2;
			r1=1;
			c1=1;
			d1=2;
		}
		int pos1=0,pos2=0;
		int strarr[][][]=new int[d1][r1][c1]; //array for user string
		int strarr1[][][]=new int[d1][r1][c1];
		int strarr2[][][]=new int[d1][r1][c1]; //array for user string
		int keyarr[][][]=new int[d1][r1][c1]; //array for the key
		int ifxfarr[][][]=new int[d1][r1][c1]; //forward feedback x-axis array
		int ibxfarr[][][]=new int[d1][r1][c1]; //backward feedback x-axis array
		int ifyfarr[][][]=new int[d1][r1][c1]; //forward feedback y-axis array
		int ibyfarr[][][]=new int[d1][r1][c1]; //backward feedback y-axis array
		int ifzfarr[][][]=new int[d1][r1][c1]; //forward feedback y-axis array
		int ibzfarr[][][]=new int[d1][r1][c1]; //backward feedback y-axis array
		
		for(k=0;k<d1;k++)   //loop for initialization of arrays
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=(int)str.charAt(pos1);
					keyarr[k][i][j]=(int)key.charAt(pos2);
					ifxfarr[k][i][j]=ifxf;
					ibxfarr[k][i][j]=ibxf;
					ifyfarr[k][i][j]=ifyf;
					ibyfarr[k][i][j]=ibyf;
					ifzfarr[k][i][j]=ifzf;
					ibzfarr[k][i][j]=ibzf;
					pos1++;
					pos2++;
				}
			}
		}
					/**************X-axis encryption****************/
		
		for(k=0;k<=d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					if(k==d1)
					{
						ibxfarr[0][0][c1-1]=ibxf;
						break;
					}
					else
						ibxfarr[k][i][c1-j-1]=ibxf;
					
					ifxf=((strarr[k][i][j]+keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256); //temporary cipher for forward feedback
					
					if(k==(d1-1) && i==(r1-1) && j==(c1-1))
					{
						ifxfarr[0][0][0]=ifxf;
					}
					else if(i==(r1-1) && j==(c1-1))
					{
						ifxfarr[k+1][0][0]=ifxf;
					}
					else if(j==(c1-1))
					{
						ifxfarr[k][i+1][0]=ifxf;
					}
					else
						ifxfarr[k][i][j+1]=ifxf;
					
					ibxf=((strarr[k][i][c1-j-1]+keyarr[k][i][c1-j-1]+ifxfarr[k][i][c1-j-1]+ibxfarr[k][i][c1-j-1])%256);  //temporary cipher for backward feedback
				count++;}
				
			}
			
		}
			/************** modified strarr after x-axis***********/
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=((strarr[k][i][j]+keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256); //initializing the cipher array
					
				count++;}
			}
		}
		
			/*************end of x-axis encryption************/
		/*System.out.print("\n");
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					System.out.print(strarr[k][i][j]+" - ");
					
				}
			}
		}*/	
			
			/************* y-axis encryption******************/
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

		for(k=0;k<=d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					if(k==d1)
					{
						ibyfarr[0][0][c1-1]=ibyf;
						break;
					}
					else
						ibyfarr[k][i][c1-j-1]=ibyf;
					
					ifyf=((strarr[k][i][j]+keyarr[k][i][j]+ifyfarr[k][i][j]+ibyfarr[k][i][j])%256); //temporary cipher for forward feedback
					
					if(k==(d1-1) && i==(r1-1) && j==(c1-1))
					{
						ifyfarr[0][0][0]=ifyf;
					}
					else if(i==(r1-1) && j==(c1-1))
					{
						ifyfarr[k+1][0][0]=ifyf;
					}
					else if(j==(c1-1))
					{
						ifyfarr[k][i+1][0]=ifyf;
					}
					else
						ifyfarr[k][i][j+1]=ifyf;
					
					ibyf=((strarr[k][i][c1-j-1]+keyarr[k][i][c1-j-1]+ifyfarr[k][i][c1-j-1]+ibyfarr[k][i][c1-j-1])%256);  //temporary cipher for backward feedback
				count++;}
				
			}
			
		}
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=((strarr[k][i][j]+keyarr[k][i][j]+ifyfarr[k][i][j]+ibyfarr[k][i][j])%256); //initializing the cipher array
					
				count++;}
			}
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
		

			/************** end of y-axis encryption****************/
		/*System.out.print("\n");
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					System.out.print(strarr[k][i][j]+" - ");
					
				}
			}
		}*/	
			/************** z-axis encryption***********************/
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
		for(k=0;k<=d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					if(k==d1)
					{
						ibzfarr[0][0][c1-1]=ibzf;
						break;
					}
					else
						ibzfarr[k][i][c1-j-1]=ibzf;
					
					ifzf=((strarr[k][i][j]+keyarr[k][i][j]+ifzfarr[k][i][j]+ibzfarr[k][i][j])%256); //temporary cipher for forward feedback
					
					if(k==(d1-1) && i==(r1-1) && j==(c1-1))
					{
						ifzfarr[0][0][0]=ifzf;
					}
					else if(i==(r1-1) && j==(c1-1))
					{
						ifzfarr[k+1][0][0]=ifzf;
					}
					else if(j==(c1-1))
					{
						ifzfarr[k][i+1][0]=ifzf;
					}
					else
						ifzfarr[k][i][j+1]=ifzf;
					
					ibzf=((strarr[k][i][c1-j-1]+keyarr[k][i][c1-j-1]+ifzfarr[k][i][c1-j-1]+ibzfarr[k][i][c1-j-1])%256);  //temporary cipher for backward feedback
				count++;}
				
			}
			
		}
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr[k][i][j]=((strarr[k][i][j]+keyarr[k][i][j]+ifzfarr[k][i][j]+ibzfarr[k][i][j])%256); //initializing the cipher array
					
				count++;}
			}
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
					ciphstr=ciphstr+(char)strarr[k][i][j];
					
				}
			}
		}
			/*********end of z-axis encryption************/
		/*System.out.print("\n");
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					System.out.print(strarr[k][i][j]+" - ");
					
				}
			}
		}*/

		int n1=r1*c1*d1;
		int n2=n1;
		int n3=n1;
      //----------encrypting rest of the characters-------------
	if(n==1)
		fos.write(strarr[0][0][0]);
	else{
		ifxf=0;
		ibxf=0;
		ifyf=0;
		ibyf=0;
		ifzf=0;
		ibzf=0;
		
		pos1=0;pos2=0;
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
						
						strarr2[k][i][j]=(int)str.charAt(n1);
						keyarr[k][i][j]=(int)key.charAt(n2);
						ifxfarr[k][i][j]=ifxf;
						ibxfarr[k][i][j]=ibxf;
						ifyfarr[k][i][j]=ifyf;
						ibyfarr[k][i][j]=ibyf;
						ifzfarr[k][i][j]=ifzf;
						ibzfarr[k][i][j]=ibzf;
						n1++;
						n2++;
						
					}
					else if(n1<n3)
					{
						strarr2[k][i][j]=(int)ciphstr.charAt(n1);
						keyarr[k][i][j]=(int)key.charAt(n2);
						ifxfarr[k][i][j]=ifxf;
						ibxfarr[k][i][j]=ibxf;
						ifyfarr[k][i][j]=ifyf;
						ibyfarr[k][i][j]=ibyf;
						ifzfarr[k][i][j]=ifzf;
						ibzfarr[k][i][j]=ibzf;
						n1++;
						n2++;
					}						
				}
			}

		}
					/**************X-axis encryption****************/
		
		for(k=0;k<=d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					if(k==d1)
					{
						ibxfarr[0][0][c1-1]=ibxf;
						break;
					}
					else
						ibxfarr[k][i][c1-j-1]=ibxf;
					
					ifxf=((strarr2[k][i][j]+keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256); //temporary cipher for forward feedback
					
					if(k==(d1-1) && i==(r1-1) && j==(c1-1))
					{
						ifxfarr[0][0][0]=ifxf;
					}
					else if(i==(r1-1) && j==(c1-1))
					{
						ifxfarr[k+1][0][0]=ifxf;
					}
					else if(j==(c1-1))
					{
						ifxfarr[k][i+1][0]=ifxf;
					}
					else
						ifxfarr[k][i][j+1]=ifxf;
					
					ibxf=((strarr2[k][i][c1-j-1]+keyarr[k][i][c1-j-1]+ifxfarr[k][i][c1-j-1]+ibxfarr[k][i][c1-j-1])%256);  //temporary cipher for backward feedback
				count++;}
				
			}
			
		}
			/************** modified strarr after x-axis***********/
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr2[k][i][j]=((strarr2[k][i][j]+keyarr[k][i][j]+ifxfarr[k][i][j]+ibxfarr[k][i][j])%256); //initializing the cipher array
					
				count++;}
			}
		}
		
			/*************end of x-axis encryption************/
		/*System.out.print("\n");
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					System.out.print(strarr2[k][i][j]+" - ");
					
				}
			}
		}*/	
			
			/************* y-axis encryption******************/
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][j][i]=strarr2[k][i][j];					
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr2[k][i][j]=strarr1[k][i][j];					
				}
			}
		}

		for(k=0;k<=d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					if(k==d1)
					{
						ibyfarr[0][0][c1-1]=ibyf;
						break;
					}
					else
						ibyfarr[k][i][c1-j-1]=ibyf;
					
					ifyf=((strarr2[k][i][j]+keyarr[k][i][j]+ifyfarr[k][i][j]+ibyfarr[k][i][j])%256); //temporary cipher for forward feedback
					
					if(k==(d1-1) && i==(r1-1) && j==(c1-1))
					{
						ifyfarr[0][0][0]=ifyf;
					}
					else if(i==(r1-1) && j==(c1-1))
					{
						ifyfarr[k+1][0][0]=ifyf;
					}
					else if(j==(c1-1))
					{
						ifyfarr[k][i+1][0]=ifyf;
					}
					else
						ifyfarr[k][i][j+1]=ifyf;
					
					ibyf=((strarr2[k][i][c1-j-1]+keyarr[k][i][c1-j-1]+ifyfarr[k][i][c1-j-1]+ibyfarr[k][i][c1-j-1])%256);  //temporary cipher for backward feedback
				count++;}
				
			}
			
		}
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr2[k][i][j]=((strarr2[k][i][j]+keyarr[k][i][j]+ifyfarr[k][i][j]+ibyfarr[k][i][j])%256); //initializing the cipher array
					
				count++;}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr1[k][i][j]=strarr2[k][j][i];					
				}
			}
		}
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr2[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		

			/************** end of y-axis encryption****************/
		/*System.out.print("\n");
		for(k=0;k<d1;k++)   
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					System.out.print(strarr2[k][i][j]+" - ");
					
				}
			}
		}*/	
			/************** z-axis encryption***********************/
		int layer3[]=new int[str.length()];
		int p2=0;
		for(i=0;i<r1;i++)
		{
			for(j=0;j<c1;j++)
			{
				for(k=0;k<d1;k++)
				{
					int tempo=strarr2[k][i][j];
					layer3[p2]=tempo;
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
					strarr1[k][i][j]=layer3[a1];	
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
					strarr2[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		for(k=0;k<=d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					
					if(k==d1)
					{
						ibzfarr[0][0][c1-1]=ibzf;
						break;
					}
					else
						ibzfarr[k][i][c1-j-1]=ibzf;
					
					ifzf=((strarr2[k][i][j]+keyarr[k][i][j]+ifzfarr[k][i][j]+ibzfarr[k][i][j])%256); //temporary cipher for forward feedback
					
					if(k==(d1-1) && i==(r1-1) && j==(c1-1))
					{
						ifzfarr[0][0][0]=ifzf;
					}
					else if(i==(r1-1) && j==(c1-1))
					{
						ifzfarr[k+1][0][0]=ifzf;
					}
					else if(j==(c1-1))
					{
						ifzfarr[k][i+1][0]=ifzf;
					}
					else
						ifzfarr[k][i][j+1]=ifzf;
					
					ibzf=((strarr2[k][i][c1-j-1]+keyarr[k][i][c1-j-1]+ifzfarr[k][i][c1-j-1]+ibzfarr[k][i][c1-j-1])%256);  //temporary cipher for backward feedback
				count++;}
				
			}
			
		}
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					strarr2[k][i][j]=((strarr2[k][i][j]+keyarr[k][i][j]+ifzfarr[k][i][j]+ibzfarr[k][i][j])%256); //initializing the cipher array
					
				count++;}
			}
		}
		
		int layer4[]=new int[str.length()];
		int p3=0;
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					int tempo1=strarr2[k][i][j];
					layer4[p3]=tempo1;
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
					strarr1[k][i][j]=layer4[q1];	
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
					strarr2[k][i][j]=strarr1[k][i][j];					
				}
			}
		}
		
		
		
		
		for(k=0;k<d1;k++)
		{
			for(i=0;i<r1;i++)
			{
				for(j=0;j<c1;j++)
				{
					newstr=newstr+(char)strarr2[k][i][j];
				}
			}
		}
		n1=r1*c1*d1;
		if(n==8)
			n2=n-n1+1;
		else
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
			newstr1=newstr1+ciphstr.charAt(i);
			i++;
		}
		i=0;
		while(i<n2)
		{				
			newstr1=newstr1+newstr.charAt(i);
			i++;
		}
		
		str=newstr1;
		for(i=0;i<str.length();i++)
		{
			fos.write(newstr1.charAt(i));
		}
		
		
	fos.close();
	}
	
	return str;
	}
}