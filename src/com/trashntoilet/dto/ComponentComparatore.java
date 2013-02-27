package com.trashntoilet.dto;

import java.util.Comparator;

public class ComponentComparatore implements Comparator<Component> {

	@Override
	public int compare(Component arg0, Component arg1) {
			    if (arg0.getDistance() <arg1.getDistance())
			        return -1;
			    else if (arg0.getDistance() >arg1.getDistance())
			     	return 1;
			    else
                    return 0;
	}

}
