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
re_list.append(re.compile(u'@牌,'));
aim_list.append(u'@牌:');
re_list.append(re.compile(u'@品牌,'));
aim_list.append(u'@品牌:');

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
		'纸牌'.decode('utf-8'):0,
		'铁牌'.decode('utf-8'):0,
		'铜牌'.decode('utf-8'):0,
		'腰牌'.decode('utf-8'):0,
		'袖牌'.decode('utf-8'):0,
		'骨牌'.decode('utf-8'):0,
		'皮牌'.decode('utf-8'):0,
		'裤牌'.decode('utf-8'):0,
		'洗牌'.decode('utf-8'):0
};
pai_pre_len3 = len('塑料牌'.decode('utf-8'));
pai_pre_dict3 = {
		'装饰牌'.decode('utf-8'):0,
		'塑料牌'.decode('utf-8'):0,
		'金属牌'.decode('utf-8'):0,
		'指示牌'.decode('utf-8'):0,
		'告示牌'.decode('utf-8'):0,
		'标价牌'.decode('utf-8'):0,
		'标记牌'.decode('utf-8'):0,
		'标识牌'.decode('utf-8'):0,
		'标志牌'.decode('utf-8'):0,
		'标示牌'.decode('utf-8'):0,
		'广告牌'.decode('utf-8'):0,
		'行李牌'.decode('utf-8'):0,
		'警示牌'.decode('utf-8'):0,
		'警告牌'.decode('utf-8'):0,
		'价格牌'.decode('utf-8'):0,
		'价钱牌'.decode('utf-8'):0,
		'麻将牌'.decode('utf-8'):0,
		'扑克牌'.decode('utf-8'):0,
		'记数牌'.decode('utf-8'):0,
		'登机牌'.decode('utf-8'):0,
		'防盗牌'.decode('utf-8'):0,
		'尺码牌'.decode('utf-8'):0,
		'促销牌'.decode('utf-8'):0,
		'发光牌'.decode('utf-8'):0,
		'玻璃牌'.decode('utf-8'):0,
		'尺码牌'.decode('utf-8'):0,
		'票价牌'.decode('utf-8'):0,
		'衣架牌'.decode('utf-8'):0,
		'工作牌'.decode('utf-8'):0
};

pai_pre_len4 = len('多米诺牌'.decode('utf-8'));
pai_pre_dict4 = {
		'多米诺牌'.decode('utf-8'):0,
		'铜版纸牌'.decode('utf-8'):0,
		'塑料插牌'.decode('utf-8'):0,
		'塑料招牌'.decode('utf-8'):0,
		'塑料胸牌'.decode('utf-8'):0,
		'塑料路牌'.decode('utf-8'):0,
		'塑料校牌'.decode('utf-8'):0,
		'塑料锁牌'.decode('utf-8'):0,
		'塑料菜牌'.decode('utf-8'):0,
		'游戏纸牌'.decode('utf-8'):0,
		'塑料路牌'.decode('utf-8'):0,
		'塑料贴牌'.decode('utf-8'):0
};

pai_pre_len5 = len('多米诺骨牌'.decode('utf-8'));
pai_pre_dict5 = {
		'多米诺骨牌'.decode('utf-8'):0,
		'塑料标示牌'.decode('utf-8'):0,
		'塑料按钮牌'.decode('utf-8'):0,
		'塑料制卡牌'.decode('utf-8'):0,
		'塑料促销牌'.decode('utf-8'):0,
		'塑料工号牌'.decode('utf-8'):0,
		'塑料证件牌'.decode('utf-8'):0,
		'塑料三角牌'.decode('utf-8'):0,
		'塑料数字牌'.decode('utf-8'):0,
		'塑料会议牌'.decode('utf-8'):0,
		'塑料提示牌'.decode('utf-8'):0,
		'塑料告示牌'.decode('utf-8'):0,
		'塑料工作牌'.decode('utf-8'):0,
		'塑料衣架牌'.decode('utf-8'):0,
		'塑料号码牌'.decode('utf-8'):0,
		'人造革胸牌'.decode('utf-8'):0,
		'重置按钮牌'.decode('utf-8'):0,
		'服装标志牌'.decode('utf-8'):0,
		'行李标签牌'.decode('utf-8'):0,
		'石膏装饰牌'.decode('utf-8'):0,
		'铁制欢迎牌'.decode('utf-8'):0,
		'铁制座位牌'.decode('utf-8'):0,
		'铁制标示牌'.decode('utf-8'):0,
		'铁制装饰牌'.decode('utf-8'):0,
		'铁制门锁牌'.decode('utf-8'):0,
		'汽车号码牌'.decode('utf-8'):0,
		'反光三角牌'.decode('utf-8'):0,
		'纸制点餐牌'.decode('utf-8'):0,
		'聚丙烯副牌'.decode('utf-8'):0,
		'聚乙烯副牌'.decode('utf-8'):0
};

pai_pre_len6 = len('全日空登机牌'.decode('utf-8'));
pai_pre_dict6 = {
		'全日空登机牌'.decode('utf-8'):0,
		'塑料制员工牌'.decode('utf-8'):0,
		'金属制装饰牌'.decode('utf-8'):0,
		'纸制人形立牌'.decode('utf-8'):0,
		'装饰用金属牌'.decode('utf-8'):0,
		'贱金属记分牌'.decode('utf-8'):0,
		'贱金属号码牌'.decode('utf-8'):0,
		'塑料制展示牌'.decode('utf-8'):0,
		'塑料制号码牌'.decode('utf-8'):0,
		'铝制指示贴牌'.decode('utf-8'):0
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
	if len(brand) >= pai_pre_len6:
		if pai_pre_dict6.has_key(s[ep-pai_pre_len6:ep]):
			return [];
	if len(brand) >= pai_pre_len5:
		if pai_pre_dict5.has_key(s[ep-pai_pre_len5:ep]):
			return [];
	if len(brand) >= pai_pre_len4:
		if pai_pre_dict4.has_key(s[ep-pai_pre_len4:ep]):
			return [];
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
model_re1 = re.compile('型号[^:;,\s\\/\)@]+'.decode('utf-8'));
model_re2 = re.compile('型号:[^:;,\s\\\/\)@]+'.decode('utf-8'));
#model_re3 = re.compile('[^\s:;,+\.\"@]+型号'.decode('utf-8'));
model_re4 = re.compile('无,型号'.decode('utf-8'));
#model_re4 = re.compile('[^\s:;,+，：；\.\"]+型'.decode('utf-8'));
def extract_remove_model(s):
	model = '';
	x = model_re4.search(s);
	if x != None:
		model = '无'.decode('utf-8');
		s = model_re4.sub('',s);
#	else: 
#		x = model_re3.search(s);
#		if x != None:
#			model = s[x.start()+1: x.end()-2]
#			s = model_re3.sub('',s);
	else: 
		x = model_re2.search(s)
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
brand_re1 = re.compile('无品牌'.decode('utf-8'));

brand_re2 = re.compile('品牌[:|;][^:;,\s\\/\)@\|]+'.decode('utf-8'));
brand_re3 = re.compile('牌子:[^:;,\s\\/\)@\|]+'.decode('utf-8'));
brand_re4 = re.compile('牌:[^:;,\s\\/\)@\|]+'.decode('utf-8'));

brand_re5 = re.compile('品牌[^\s:;,\+\.\"\\/@\|]+'.decode('utf-8'));
brand_re6 = re.compile('牌子[^\s:;,\+\.\"\\/@\|]+'.decode('utf-8'));

brand_re7 = re.compile('[,;@%:\s\(\)][^,;:@\s\(\)\\/%]+?牌'.decode('utf-8'));
brand_re8 = re.compile('[,;@%:\s\(\)][^,;:@\s\(\)\\/%]+?牌子'.decode('utf-8'));
brand_re9 = re.compile('^[^:;,\s\\/\"%\(]+?牌'.decode('utf-8'));


#pattern1 = re.compile(u'品|\"|型号')
brand_len2 = len('品牌'.decode('utf-8'));
brand_len1 = len('牌'.decode('utf-8'));

def extract_remove_brand(s):
	brand = '';
	x = brand_re1.search(s);
	if x != None:
		brand = '无'.decode('utf-8');
		s = brand_re1.sub('',s);
	else:
		x = brand_re2.search(s);
		if x != None:
			brand = s[x.start()+brand_len2+1:x.end()];
			s = brand_re2.sub('',s);
		else:
			x = brand_re3.search(s);
			if x != None:
				brand = s[x.start()+brand_len2+1: x.end()];
				s = brand_re3.sub('',s);
			else:
				x = brand_re4.search(s);
				if x != None:
					brand = s[x.start()+brand_len1+1: x.end()];
					s = brand_re4.sub('',s);
				else:
					x = brand_re5.search(s);
					if x != None:
						brand = s[x.start()+brand_len2: x.end()];
						s = brand_re5.sub('',s);
					else:
						x = brand_re6.search(s);
						if x != None:
							brand = s[x.start()+brand_len2: x.end()];
							s = brand_re6.sub('',s);
						else:
							x = brand_re7.search(s);
							if x != None:
								if check_brand(s,x.start()+1,x.end()): 
									brand = s[x.start()+1:x.end()-1];
									s = brand_re7.sub('',s);
							else:
								x = brand_re8.search(s);
								if x!= None:
									brand = s[x.start()+1:x.end()-2];
									s = brand_re8.sub('',s);
								else:
									x = brand_re9.search(s);
									if x != None:
										if check_brand(s,x.start(),x.end()): 
											brand = s[x.start():x.end()-1];
											s = brand_re9.sub('',s);

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

		y = normalize_string(tmp);

#		z = detect_remove_kv(y);

		x = compute(y);
		
		if x[0] != '':
#			out.write('品牌:');
			out.write(x[0].encode(encoding)+'@@');
		else:
#			out.write('品牌:无'+'||');
			out.write('无'+'@@');
		if x[1] != '':
#			out.write('型号:');
			out.write(x[1].encode(encoding)+'@@');
		else:
#			out.write('型号:无'+'\t');
			out.write('无'+'@@');

		y = cleaner(x[2]);
#		out.write('其他描述:' + y.encode(encoding)+'\n');
		out.write(y.encode(encoding)+'\n');
			
		
	i.close()
	out.close()


def cleaner(s):
	s = re.sub('\(,',',',s)
#	s = re.sub('\)@','@',s)
	s = re.sub('^[,]+','',s)
	s = re.sub('[,]+$','',s)
	s = re.sub('[,]{2,}',',',s)
	s = s.rstrip('|');
	s = re.sub('^@','',s)
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
