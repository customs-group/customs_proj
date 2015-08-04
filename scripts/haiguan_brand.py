#!/usr/bin/python2.7
# -*- coding: utf-8 -*-

"""
Created on Thu Apr 03 09:30:10 2014

@author: zhengzhong.zz
"""

import re
import langconv
import pdb
#import string;


re_list = [];
aim_list = [];

#replace patterns
"""
re_list.append(re.compile('ａ|Ａ|A'.decode('utf-8')));
re_list.append(re.compile('ｂ|Ｂ|B'.decode('utf-8')));
re_list.append(re.compile('ｃ|Ｃ|C'.decode('utf-8')));
re_list.append(re.compile('ｄ|Ｄ|D'.decode('utf-8')));
re_list.append(re.compile('ｅ|Ｅ|E'.decode('utf-8')));
re_list.append(re.compile('ｆ|Ｆ|F'.decode('utf-8')));
re_list.append(re.compile('ｇ|Ｇ|G'.decode('utf-8')));
re_list.append(re.compile('ｈ|Ｈ|H'.decode('utf-8')));
re_list.append(re.compile('ｉ|Ｉ|I'.decode('utf-8')));
re_list.append(re.compile('ｊ|Ｊ|J'.decode('utf-8')));
re_list.append(re.compile('ｋ|Ｋ|K'.decode('utf-8')));
re_list.append(re.compile('ｌ|Ｌ|L'.decode('utf-8')));
re_list.append(re.compile('ｍ|Ｍ|M'.decode('utf-8')));
re_list.append(re.compile('ｎ|Ｎ|N'.decode('utf-8')));
re_list.append(re.compile('ｏ|Ｏ|O'.decode('utf-8')));
re_list.append(re.compile('ｐ|Ｐ|P'.decode('utf-8')));
re_list.append(re.compile('ｑ|Ｑ|Q'.decode('utf-8')));
re_list.append(re.compile('ｒ|Ｒ|R'.decode('utf-8')));
re_list.append(re.compile('ｓ|Ｓ|S'.decode('utf-8')));
re_list.append(re.compile('ｔ|Ｔ|T'.decode('utf-8')));
re_list.append(re.compile('ｕ|Ｕ|U'.decode('utf-8')));
re_list.append(re.compile('ｖ|Ｖ|V'.decode('utf-8')));
re_list.append(re.compile('ｗ|Ｗ|W'.decode('utf-8')));
re_list.append(re.compile('ｘ|Ｘ|X'.decode('utf-8')));
re_list.append(re.compile('ｙ|Ｙ|Y'.decode('utf-8')));
re_list.append(re.compile('ｚ|Ｚ|Z'.decode('utf-8')));
aim_list.append('a');
aim_list.append('b');
aim_list.append('c');
aim_list.append('d');
aim_list.append('e');
aim_list.append('f');
aim_list.append('g');
aim_list.append('h');
aim_list.append('i');
aim_list.append('j');
aim_list.append('k');
aim_list.append('l');
aim_list.append('m');
aim_list.append('n');
aim_list.append('o');
aim_list.append('p');
aim_list.append('q');
aim_list.append('r');
aim_list.append('s');
aim_list.append('t');
aim_list.append('u');
aim_list.append('v');
aim_list.append('w');
aim_list.append('x');
aim_list.append('y');
aim_list.append('z');
"""

re_list.append(re.compile('０'.decode('utf-8')));
re_list.append(re.compile('１'.decode('utf-8')));
re_list.append(re.compile('２'.decode('utf-8')));
re_list.append(re.compile('３'.decode('utf-8')));
re_list.append(re.compile('４'.decode('utf-8')));
re_list.append(re.compile('５'.decode('utf-8')));
re_list.append(re.compile('６'.decode('utf-8')));
re_list.append(re.compile('７'.decode('utf-8')));
re_list.append(re.compile('８'.decode('utf-8')));
re_list.append(re.compile('９'.decode('utf-8')));
aim_list.append('0');
aim_list.append('1');
aim_list.append('2');
aim_list.append('3');
aim_list.append('4');
aim_list.append('5');
aim_list.append('6');
aim_list.append('7');
aim_list.append('8');
aim_list.append('9');

re_list.append(re.compile(' +'.decode('utf-8')));
aim_list.append(',');
re_list.append(re.compile('％'.decode('utf-8')));
aim_list.append('%');
re_list.append(re.compile('：'.decode('utf-8')));
aim_list.append(':');
re_list.append(re.compile('．|\.|\·|。'.decode('utf-8')));
aim_list.append('.');
re_list.append(re.compile('＊|\*'.decode('utf-8')));
aim_list.append('*');
re_list.append(re.compile('\_|－|＿|\-|\_'.decode('utf-8')));
aim_list.append('-');
re_list.append(re.compile('＂|\"|“|”'.decode('utf-8')));
aim_list.append('\"');
re_list.append(re.compile('＆|\&'.decode('utf-8')));
aim_list.append('&');
re_list.append(re.compile('＋|\+'.decode('utf-8')));
aim_list.append('+');
#re_list.append(re.compile(' |｀|～|！|＠|＄|＾|＝|＼|｜|；|＇|，|／|？|\`|\~|\!|\@|\$|\^|\=|\\\\|\||\;|\'|\,|\/|\?|！|￥|…|、|；|’|‘|，|、|？|\#'.decode('utf-8')));
#aim_list.append(',');

re_list.append(re.compile(';|、|；|\/|、| |，|｀|～|！|＠|＄|＾|＝|＼|｜|＇|／|？'.decode('utf-8')));
aim_list.append(',');

re_list.append(re.compile('[;,]+'.decode('utf-8')));
aim_list.append(',');

#re_list.append(re.compile('[0-9a-zA-Z][\u4e00-\u9fa5]|[\u4e00-\u9fa5][0-9a-zA-Z]'.decode('utf-8')));
#aim_list.append(',');

re_list.append(re.compile('（|（|［|｛|\(|\[|\{|【|\{'.decode('utf-8')));
re_list.append(re.compile('）|）|］|｝|\)|\]|\}|】|\}'.decode('utf-8')));
aim_list.append('(');
aim_list.append(')');

re_list.append(re.compile('＜|\<|≤|《'.decode('utf-8')));
re_list.append(re.compile('＞|\>|≥|》'.decode('utf-8')));
aim_list.append('<');
aim_list.append('>');


#business patterns

#re_list.append(re.compile('\([^)]*\)|\([^)]*$|,[0-9a-z*,-]*$|无品牌.*$'.decode('utf-8')));
#aim_list.append(',,无品牌,,');

re_list.append(re.compile(u'^无品牌型号,'));
aim_list.append(u'无品牌,无型号,');

re_list.append(re.compile(u'无品牌型号$'));
aim_list.append(u',无品牌,无型号');


re_list.append(re.compile(',\+'));
aim_list.append(',');

list_len = len(re_list);



pai_pre_len2 = len('吊牌'.decode('utf-8'));
pai_pre_dict2 = {
		'吊牌'.decode('utf-8'):0,
		'门牌'.decode('utf-8'):0,
		'车牌'.decode('utf-8'):0,
		'铭牌'.decode('utf-8'):0,
		'名牌'.decode('utf-8'):0,
		'挂牌'.decode('utf-8'):0,
		'标牌'.decode('utf-8'):0,
		'奖牌'.decode('utf-8'):0,
		'无牌'.decode('utf-8'):0,
};

pai_pre_len3 = len('塑料牌'.decode('utf-8'));
pai_pre_dict3 = {
		'塑料牌'.decode('utf-8'):0,
		'指示牌'.decode('utf-8'):0,
		'标价牌'.decode('utf-8'):0,
		'标记牌'.decode('utf-8'):0,
		'标识牌'.decode('utf-8'):0,
		'标志牌'.decode('utf-8'):0,
		'广告牌'.decode('utf-8'):0,
		'行李牌'.decode('utf-8'):0,
		'警示牌'.decode('utf-8'):0,
		'价格牌'.decode('utf-8'):0,
		'价钱牌'.decode('utf-8'):0,
		'麻将牌'.decode('utf-8'):0,
		'扑克牌'.decode('utf-8'):0,
};

pai_post_len1 = len('照'.decode('utf-8'));
pai_post_dict1 = {
		'照'.decode('utf-8'):0,
		'号'.decode('utf-8'):0,
};

def normalize_string(s):
	"""
		normalize input string
	"""
	for i in range(0,list_len):
		s = re.sub(re_list[i], aim_list[i], s);

	s = langconv.Converter('zh-hans').convert(s);
	return s;


# Validate brand
def check_brand(s, sp, ep):
	"""
	s: original string
	sp: brand start pos
	ep: brand end pos
	"""
	brand = s[sp:ep];
	if len(brand) >= pai_pre_len3:
		if pai_pre_dict3.has_key(s[ep-pai_pre_len3:ep]):
			return [];
	if len(brand) >= pai_pre_len2:
		if pai_pre_dict2.has_key(s[ep-pai_pre_len2:ep]):
			return [];
	if len(s) >= len(brand) + pai_post_len1:
		if pai_post_dict1.has_key(s[ep:ep+pai_post_len1]):
			return [];
	return True;


lenpinpai = len(u'品牌:');
kv_re = re.compile('^[^:,]{2}:[^:,]+|,[^:,]{2}:[^:,]+'.decode('utf-8')); 
kv_test = re.compile('^[^:,]{2}:[^:,]+$');

def detect_remove_kv(s):
	x = kv_re.findall(s);
	brand = '';
	model = '';
	for i in range(len(x)):
		if x[i][0] == ',' : x[i] = x[i][1:];
		if x[i][:lenpinpai] == u'品牌:':
			brand = x[i][lenpinpai:];
		if x[i][:lenpinpai] == u'型号:':
			model = x[i][lenpinpai:];
	
	y = kv_test.search(s);
	if y != None and y.start() == 0 and y.end() == len(s):
		return [x, brand, model, s];
	else:
		s = kv_re.sub('', s);
		return [x, brand, model, s];

brand_re4 = re.compile(u'品牌:[^:,]+');
def detect_remove_brand_comma(s):
	brand = '';
	x = brand_re4.search(s);
	if x != None:
		if check_brand(s, x.start()+lenpinpai, x.end()):
			brand = s[x.start()+lenpinpai: x.end()];
			s = brand_re4.sub('', s);

	return [brand, s];


brand_re3 = re.compile(u'品牌[^:,+\.\"]+');
def detect_remove_brand_nocomma(s):
	x = brand_re3.search(s);
	brand = '';
	if x != None:
		if check_brand(s, x.start()+pai_pre_len2, x.end()):
			brand = s[x.start()+pai_pre_len2: x.end()];
			s = brand_re3.sub('', s);

	return [brand, s];

brand_test_re = re.compile('[0-9a-z,*&%+-]*');
brand_re1 = re.compile(',[^,]*?牌'.decode('utf-8'));	#,xxx牌这种格式。如果之前全为数字字母，留后面的，反之留前面的
brand_re2 = re.compile('^.*?牌'.decode('utf-8')); 		#最前端的XX牌
def detect_remove_brand(s):
	"""
		if brand detected, return [brand, string_without_brand];
		else, return []
	"""
	brand = '';
	x = brand_re1.search(s);
	if x != None:
		if check_brand(s, x.start()+1, x.end()):
			brand = s[x.start()+1: x.end()];
			y = brand_test_re.search(s[:x.start()]);
			if y != None and y.start() == 0 and y.end() == x.start():
				return [s[:x.end()-pai_post_len1], s[x.end():]];
			else:
				return [brand[:len(brand)-pai_post_len1], s[:x.start()]];

	x = brand_re2.search(s);
	if x != None:
		if check_brand(s, x.start(), x.end()):
			brand = s[x.start():x.end()];
			if len(s) >= x.end() + pai_pre_len2:
				return [brand[:len(brand)-pai_post_len1], s[x.end():]];
	
	return ['', s];

model_re1 = re.compile(u'型号[^,]+');
def detect_remove_model(s):
	model = '';
	x = model_re1.search(s);
	if x != None:
		model = s[x.start() + pai_pre_len2:x.end()];
		if len(model) < 1:
			return ['', s];

		if model[0] == ':' : model = model[1:];

		if x.start() == 0:
			if len(s[x.end():]) > 1:
				return [model, s[x.end():]];
		else:
			if len(s[:x.start()]) > 1:
				return [model, s[:x.start()]];

	return ['', s];


def find_brand(s):

	x = detect_remove_kv(s);
	brand = x[1];
	model = x[2];
	s = x[3];
	
	#did not find brand in detect_remove_kv()
	if brand == '':
		y = detect_remove_brand_comma(s);
		brand = y[0];
		s = y[1];
		if brand == '':
			y = detect_remove_brand_nocomma(s);
			brand = y[0];
			s = y[1];
			if brand == '':
				y = detect_remove_brand(s);
				brand = y[0];
				s = y[1];

	if model == '':
		y = detect_remove_model(s);
		model = y[0];
		s = y[1];
		if model == '' and brand != '':
			y = detect_remove_model(brand);
			model = y[0];
			brand = y[1];


	return [brand, model, s];

def find_refine_brand(s):
	"""
		combine find_brand
		xx[0]: brand
		xx[1]: model
		xx[2]: commodity
	"""
	xx = find_brand(s);
	#if xx == []: return [];
	if xx[0] != '': 
		if xx[0][0] == "\"":
			xx[0] = xx[0][1:];
	if xx[0] != '':
		if xx[0][-1] == "\"":
			xx[0] = xx[0][:-1];
	if xx[0] != '':
		for i in range(len(xx[0])-1, -1, -1):
			if xx[0][i] == ',' or xx[0][i] == ':' or xx[0][i] == '\"':
				xx[0] = xx[0][i+1:];
				break;
	if len(xx[0]) > pai_pre_len2 and xx[0][-pai_pre_len2:] == u'型号':
		xx[0] = xx[0][:-pai_pre_len2];
	if len(xx[0]) > pai_post_len1 and xx[0][-pai_post_len1:] == u'品':
		if len(xx[0]) > pai_pre_len2 and xx[0][-pai_pre_len2] == u'一':
			pass;
		else:
			xx[0] = xx[0][:-pai_post_len1];


	if len(xx[0]) > pai_post_len1 and xx[0][-pai_post_len1:] == u'无':
		xx[0] = xx[0][:-pai_post_len1];

	if xx[1] != '':
		if xx[1][0] == "\"":
			xx[1] = xx[1][1:];
	if xx[1] != '':
		if xx[1][-1] == "\"":
			xx[1] = xx[1][:-1];
	if xx[1] != '':
		for i in range(len(xx[1])):
			if xx[1][i] == ',' or xx[1][i] == ':':
				xx[1] = xx[1][:i];
				break;

	if xx[2] != '':
		if xx[2][0] == ',':
			xx[2] = xx[2][1:];
	if xx[2] != '':
		if xx[2][-1] == ',':
			xx[2] = xx[2][:-1];

	return xx;
	


def normalize_file(input, output, encoding = "utf-8"):
	try:
		f = open(input, 'r');
		g = open(output, 'w+');

		while True:
			try:
				#pdb.set_trace();
				x = f.readline();
				if not x : break;
				y = x.rstrip('\n').decode(encoding);
				if y[0] == "\"" : y=y[1:];
				if y[-1] == "\"" : y=y[0:-2];
				x = normalize_string(y);
				if x == '' : 
					g.write(u'无\t无\t'+y.encode(encoding)+'\t'+y.encode(encoding)+'\n');
					continue;
					
				z = find_refine_brand(x);

				brand = z[0];
				model = z[1];
				s = z[2];

				if brand == '': brand = u'无';
				if model == '': model = u'无';
				if s == '': s = "\\N";

				g.write(brand.encode(encoding)+'\t'+model.encode(encoding)+'\t'+s.encode(encoding)+'\t'+x.encode(encoding)+'\n');

		 	except Exception:
				print "error happens at normalize file"+x;
				pdb.set_trace();

	finally:
		f.close();
		g.close();



#############################################################################
#############################################################################

except_dict = {
		'塑料制'.decode('utf-8'):0,
		'太阳'.decode('utf-8'):0,
		'am'.decode('utf-8'):0,
		};
brand_dict = {};

def read_brand(input, encoding = 'utf-8'):

	try:
		f = open(input, 'r');
		while True:
			try:
				#pdb.set_trace();
				x = f.readline();
				if not x : break;
				y = x.rstrip('\n').decode(encoding).split('\t');
				if y[0] == "\\N" or len(y[0]) < 2:	continue;
				if except_dict.has_key(y[0]): continue;
				if len(y[0]) == 2 and y[0].isalpha(): continue;

				#y[0] is detected brand
				l = len(y[0]);
				if not brand_dict.has_key(l):
					brand_dict[l] = {};

				brand_dict[l][y[0]] = 1;

	 		except Exception:
				print "error happens at "+x;
				#pdb.set_trace();
	finally:
		f.close();


def remove_brand(input, output, encoding = 'utf-8'):
	"""
		input format: brand model commodity g_name
	"""
	try:
		f = open(input, 'r');
		g = open(output, 'w+');

		s = sorted(brand_dict.iteritems(), key = lambda a:a[0], reverse = True);

		while True:
			x = f.readline();
			if not x: break;
			y = x.rstrip('\n').decode(encoding).split('\t');
			if y[0] != '\\N':
				g.write(x);
				continue;

			z = y[3];

			brand = y[0];
			model = y[1];
			commodity = y[2];

			for it in s:
				if it[1].has_key(z[:it[0]]):
					brand = z[:it[0]];
					commodity = z[it[0]:];
					break;

			g.write(brand.encode(encoding)+'\t'+model.encode(encoding)+'\t'+commodity.encode(encoding)+'\t'+y[3].encode(encoding)+'\n');

	finally:
		f.close();
		g.close();


def write_brand_dict(output, encoding = 'utf-8'):

	try:
		g = open(output, 'w+');

		s = sorted(brand_dict.iteritems(), key = lambda a:a[0]);
		for key1 in s:
			try:
				t = sorted(key1[1].iteritems(), key = lambda a:a[0]);
				for k in t:
					g.write(k[0].encode(encoding)+'\n');
			except Exception:
				print "error happens at " + key1[1][0];
	finally:
		g.close();

			

#param1: input
#param2: output
#param3: name or model
#param4: optional, encoding

if __name__ == '__main__':
	import sys;
	import os;

	if len(sys.argv) < 4:
		exit;

	input = sys.argv[1];
	output = sys.argv[2];
	model = sys.argv[3];
	encoding = "utf-8";

	if len(sys.argv) >= 5:
		encoding = sys.argv[4];


	if model == "name":
		tmp = "output_1";
		normalize_file(input, tmp, encoding);
		read_brand(tmp, encoding);
		remove_brand(tmp, output, encoding);

		os.remove(tmp);
	else:
		normalize_file(input, output, encoding);


