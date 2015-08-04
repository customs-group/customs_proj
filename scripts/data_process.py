#!/usr/bin/python2.7
#encoding=utf-8
"""


Author:Wangweidi， LuPeng
"""

import re
import string
import langconv

re_list = [];
aim_list = [];

#replace patterns
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
re_list.append(re.compile(';|、|；|\/|、| |，|｀|～|！|＠|＄|＾|＝|＼|｜|＇|／|？'.decode('utf-8')));
aim_list.append(',');

re_list.append(re.compile('（|（|［|｛|\(|\[|\{|【|\{'.decode('utf-8')));
re_list.append(re.compile('）|）|］|｝|\)|\]|\}|】|\}'.decode('utf-8')));
aim_list.append('(');
aim_list.append(')');
re_list.append(re.compile('＜|\<|≤|《'.decode('utf-8')));
re_list.append(re.compile('＞|\>|≥|》'.decode('utf-8')));
aim_list.append('<');
aim_list.append('>');

re_list.append(re.compile(u'^无品牌型号'));
aim_list.append(u'无品牌,无型号,');
re_list.append(re.compile(u'^无品牌无型号'));
aim_list.append(u'无品牌,无型号,');
re_list.append(re.compile(u'无品牌型号$'));
aim_list.append(u',无品牌,无型号');
re_list.append(re.compile(u'无品牌无型号$'));
aim_list.append(u',无品牌,无型号');
re_list.append(re.compile(u'无品牌型号'));
aim_list.append(u'无品牌,无型号,');
re_list.append(re.compile(u'[^\s]无品牌无型号'));
aim_list.append(u',无品牌,无型号,');
re_list.append(re.compile(u'[^\s]型号'));
aim_list.append(u',型号:');

re_list.append(re.compile('[;,]+'.decode('utf-8')));
aim_list.append(',');


#re_list.append(re.compile(',\+'));
#aim_list.append(',');

list_len = len(re_list);

def normalize_string(s):
	for i in range(0,list_len):
		s = re.sub(re_list[i], aim_list[i], s);
	s = langconv.Converter('zh-hans').convert(s);
	return s;



len_pre_kv = len(u'品牌:');
kv_re = re.compile('^[^:,]:[^:,]+|,[^:,]:[^:,]+'.decode('utf-8')); 
kv_test = re.compile('^[^:,]:[^:,]+$');

def detect_remove_kv(s):
	x = kv_re.findall(s);
	brand = '';
	model = '';
	for i in range(len(x)):
		if x[i][0] == ',' : x[i] = x[i][1:];
		if x[i][:len_pre_kv] == u'品牌:':
			brand = x[i][len_pre_kv:];
		if x[i][:len_pre_kv] == u'型号:':
			model = x[i][len_pre_kv:];
	
	y = kv_test.search(s);
	if y != None and y.start() == 0 and y.end() == len(s):
		return [x, brand, model, s];
	else:
		s = kv_re.sub('', s);
		return [x, brand, model, s];


#过滤关键词
pai_pre_len2 = len('品牌'.decode('utf-8'));
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


#品牌/型号匹配

#re1 = 型号xxx
#re2 = 型号: xxx
model_re1 = re.compile('型号[^\s:;,+\.\"]+'.decode('utf-8'));
model_re2 = re.compile('型号[:][^:;,\s]+'.decode('utf-8'));
model_re3 = re.compile('[^\s:;,+\.\"]+型号'.decode('utf-8'));
#model_re4 = re.compile('[^\s:;,+，：；\.\"]+型'.decode('utf-8'));
def extract_remove_model(s):
	model = '';
	x = model_re3.search(s);
	if x != None:
		model = s[x.start()+1: x.end()-2]
		s = model_re3.sub('',s);
	else:
		x = model_re2.search(s);
		if x != None:
			model = s[x.start()+pai_pre_len2+1: x.end()]
			s = model_re2.sub('',s);
		else:
			x = model_re1.search(s)
			if x != None:
				model = s[x.start()+pai_pre_len2 : x.end()];
				s = model_re1.sub('',s);
	if len(model) < 1:	model = '';

	return [model, s]

#re1 = /s xx牌
#re2 = xx牌
#re3 = 品牌(无标点)xxx
#re4 = 品牌:xxx
brand_re1 = re.compile('[\(:;,\s][^:;,\s\\/]*?牌'.decode('utf-8'));
brand_re2 = re.compile('^[^:;,\s\\/\"]*?牌'.decode('utf-8')); 
brand_re3 = re.compile(u'品牌[^\s:;,\+\.\"\\/]+');
brand_re4 = re.compile(u'品牌[:][^:;,\s\\/]+');
brand_re5 = re.compile(u'无牌|无品牌');
#pattern1 = re.compile(u'品|\"|型号')

def extract_remove_brand(s):
	brand = '';
	x = brand_re5.search(s)
	if x != None:
		brand = '无'.decode('utf-8');
		s = brand_re5.sub('',s)
	else:
		x = brand_re4.search(s)
		if x != None:
			brand = s[x.start()+pai_pre_len2+1:x.end()]
			s = brand_re4.sub('',s)
		else:
			x = brand_re3.search(s)
			if x != None:
				brand = s[x.start()+pai_pre_len2: x.end()]
				s = brand_re3.sub('',s)
			else:
				x = brand_re1.search(s)
				if x != None:
					if check_brand(s,x.start()+1,x.end()): 
						brand = s[x.start()+1:x.end()-1];
						s = brand_re1.sub('',s)
				else:
					x = brand_re2.search(s)
					if x != None:
						if check_brand(s,x.start(),x.end()): 
							brand = s[x.start():x.end()-1]
							s = brand_re2.sub('',s)

	if len(brand) < 1: 	brand = ''
#	brand = pattern1.sub('',brand)
	brand = brand.lstrip('\"');
	brand = brand.rstrip('\"');
	
	return [brand,s]


model_re = re.compile('^[0-9A-Za-z,\- \*\.\+%:\(\)\"=#\<\>]+$'.decode('utf-8'));

def compute(s):
	brand = '';
	model = '';

	x = extract_remove_brand(s);
	brand = x[0];
	s = x[1];

	x = extract_remove_model(s);
	model = x[0];
	s = x[1];

	result = model_re.search(s);
	if result != None :
		model += s;
		s = model_re.sub('',s);
	
	return [brand,model,s]



def extractor(in_file,out_file,encoding = 'utf-8'):
	i = open(in_file,'r')
	out = open(out_file,'w+')
	
	
	while True:
		x = i.readline();
		if not x : break;
		tmp = x.rstrip('\n').decode(encoding);
		if tmp[0] == '\"': tmp = tmp[1:];

		if len(tmp)==0: continue;

		if tmp[-1] == '\"': tmp = tmp[0:-1];

		y = normalize_string(tmp);

#		z = detect_remove_kv(y);

		x = compute(y);
		
		if x[0] != '':
			out.write('品牌:');
			out.write(x[0].encode(encoding)+'\t');
		else:
			out.write('品牌:无'+'\t');
		if x[1] != '':
			out.write('型号:');
			out.write(x[1].encode(encoding)+'\t');
		else:
			out.write('型号:无'+'\t');

		y = cleaner(x[2]);
#		out.write(tmp.encode(encoding)+'\t');
		out.write('其他描述:' + y.encode(encoding)+'\n');
			
		
	i.close()
	out.close()


def cleaner(s):
	#s = s.decode(encoding);
	s = re.sub('^[,]+','',s)
	s = re.sub('[,]+$','',s)
	s = re.sub('[,]{2,}',',',s)
	return s;

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
	extractor(in_file,out_file,encoding)
