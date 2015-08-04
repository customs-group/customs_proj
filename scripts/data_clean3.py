#!/usr/bin/python2.7
#coding=utf-8
"""
Created on Wed Jue 20 2015  15:40

Author:Wangweidi
"""

import re
import string

def data_clean(in_file,out_file,encoding = 'utf-8'):
	f = open(in_file,'r')
	g = open(out_file,'w+')
	pattern = re.compile("\d[\d|.|\-|/|\s]*[a-zA-Z|.|\-|/|\s]+[\w|.|\-|/|\s]*\w|[a-zA-Z][a-zA-Z|.|\-|/|\s]*[\d|.|\-|/|\s]+[\w|.|\-|/|\s]*\w|\w+[.|\-|/|\s]\d+".decode(encoding))
	pattern1 = re.compile("\d*[a-zA-Z]*系列".decode(encoding))
	while True:
		x = f.readline()
		if not x : break
		y = x.rstrip('\n').decode(encoding)
		a = y
		m = pattern.search(y)
		n = pattern1.search(y)
		x = re.sub(pattern,"",y)
		y = re.sub(pattern1,"",x)
		g.write(a.encode(encoding) +"\t\t\t\t")
		g.write(y.encode(encoding) +':')
		if n:
			z = n.group(0)
			g.write(z.encode(encoding) +':')
		if m:
			z = m.group(0)
			g.write(z.encode(encoding) + '\n')
		else:
			g.write('\n')
	f.close()
	g.close()


#param1: input
#param2: output
#param3: encoding,optional

if __name__ == '__main__':
	import sys
	import os
	
	if len(sys.argv) < 3:
		exit 
	in_file = sys.argv[1]
	out_file = sys.argv[2]
	encoding = 'utf-8'
	
	if len(sys.argv) >=4:
		encoding = sys.argv[3]
	data_clean(in_file,out_file,encoding)
