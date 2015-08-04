#!/usr/bin/python2.7
# -*- coding: utf-8 -*-

"""
Created on Thu Apr 03 09:30:10 2014

@author: zhengzhong.zz
"""

import re
import string;

def norm_file(input, output, encoding = 'utf-8'):
	try:
		f = open(input, 'r');
		g = open(output, 'w+');

		while True:
			x = f.readline();
			if not x : break;
			y = x.rstrip('\n').decode(encoding);
			while y[-1] == "\\":
				z = f.readline();
				if not z: break;
				zz = z.rstrip('\n').decode(encoding);
				y = y[:-1] + zz;
			g.write(y.encode(encoding) + '\n');

	finally:
		f.close();
		g.close();
				
			

#param1: input
#param2: output
#param3: optional, encoding

if __name__ == '__main__':
	import sys;
	import os;

	if len(sys.argv) < 3:
		exit;

	input = sys.argv[1];
	output = sys.argv[2];
	encoding = "utf-8";

	if len(sys.argv) >= 4:
		encoding = sys.argv[3];

	norm_file(input, output, encoding);



