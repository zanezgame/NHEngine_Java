package nicehu.nhsdk.candy.random;

import java.util.ArrayList;
import java.util.List;

import nicehu.nhsdk.candy.struct.Pair;

public class CandyRandom
{
	private List<Pair<Double, Object>> datas = new ArrayList<Pair<Double, Object>>();;
	private double totalValue = 0;
	private boolean genTotalValue;

	public void add(Object data)
	{
		genTotalValue = false;
		datas.add(new Pair<Double, Object>(1.0, data));
	}

	public void add(double value, Object data)
	{
		genTotalValue = false;
		datas.add(new Pair<Double, Object>(value, data));
	}

	public Object Random()
	{
		int index = RandomIndex();
		if (index == -1)
		{
			return null;
		}
		return this.get(index).getSecond();
	}

	public Object RandomAndRemove()
	{
		int index = RandomIndex();
		if (index == -1)
		{
			return null;
		}

		Pair<Double, Object> pair = this.get(index);
		this.remove(index);
		return pair.getSecond();
	}

	private int RandomIndex()
	{
		if (!genTotalValue)
		{
			this.genTotalValue();
		}
		double randomValue = RandomU.randomDouble(0, totalValue);
		double totalValue = 0;
		for (int i = 0; i < datas.size(); i++)
		{
			totalValue += datas.get(i).getFirst();
			if (randomValue < totalValue)
			{
				return i;
			}
		}
		return -1;
	}

	private void genTotalValue()
	{
		this.totalValue = 0;
		for (int i = 0; i < datas.size(); i++)
		{
			this.totalValue += datas.get(i).getFirst();
		}
	}

	public Pair<Double, Object> get(int index)
	{
		return datas.get(index);
	}

	public void remove(int index)
	{
		genTotalValue = false;
		datas.remove(index);
	}

	public void Clear()
	{
		genTotalValue = false;
		datas.clear();
	}

	public boolean isEmpty()
	{
		return datas.isEmpty();
	}

	public int valueSize()
	{
		return datas.size();
	}

}
