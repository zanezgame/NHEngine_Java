package nicehu.nhsdk.candy.struct;

public class Pair3<T1, T2, T3>
{
	private T1 t1;
	private T2 t2;
	private T3 t3;

	public Pair3()
	{
	}

	public Pair3(T1 t1, T2 t2, T3 t3)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}

	public T1 getFirst()
	{
		return t1;
	}

	public void setFirst(T1 t1)
	{
		this.t1 = t1;
	}

	public T2 getSecond()
	{
		return this.t2;
	}

	public void setSecond(T2 t2)
	{
		this.t2 = t2;
	}

	public T3 getThird()
	{
		return this.t3;
	}

	public void setThird(T3 t3)
	{
		this.t3 = t3;
	}
}
