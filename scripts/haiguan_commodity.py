#!/usr/bin/python2.7
# -*- coding: utf-8 -*-

"""
Created on Thu Apr 03 09:30:10 2014

@author: zhengzhong.zz
"""

import re
import pdb;
import string;

c_dict = {};  #key: code_ts, country, commodity; value: commodity id
cc_dict = {}; #key: code_ts; value: max commodity id of same key

def sort_string(s):
	t = ''.join(sorted(s));
	return t;

def read_commodity(input, output, encoding = 'utf-8'):
	"""
		format of input:
		entry_id g_no code_ts g_name g_model origin_country brand model commodity normalized_g_name

		columns 3, 6, 8 and 9 are used to build dict key
	"""

	try:
		f = open(input, 'r');
		g = open(output, 'w+');

		while True:
			try:
				#pdb.set_trace();
				x = f.readline();
				if not x : break;
				y = x.rstrip('\n').decode(encoding).split('\t');

				t = (y[2], y[5], y[7], sort_string(y[8]));	#code_ts, country, model, commodity
				id = 1;
				if not c_dict.has_key(t): #commodity is first detected
					if cc_dict.has_key(y[2]):
						cc_dict[y[2]] += 1;
						id = cc_dict[y[2]];
					else:
						cc_dict[y[2]] = id;

					c_dict[t] = id;
				else:
					id = c_dict[t];

				id_str = y[2] + str(id).zfill(5);

				g.write(y[0].encode(encoding)+'\t'+y[1].encode(encoding)+'\t'+y[2].encode(encoding)+'\t'+y[3].encode(encoding)+'\t'+y[4].encode(encoding)+'\t'+y[5].encode(encoding)+'\t'+y[6].encode(encoding)+'\t'+y[7].encode(encoding)+'\t'+y[8].encode(encoding)+'\t'+id_str.encode(encoding)+'\n');
			
			except Exception:
				print "error happens at " + x;

	finally:
		f.close();
		g.close();


if __name__ == '__main__':
	import sys;

	if len(sys.argv) < 3:
		exit;

	input = sys.argv[1];
	output = sys.argv[2];
	encoding = 'utf-8';

	if len(sys.argv) >= 4:
		encoding = sys.argv[3];

	read_commodity(input, output, encoding);
