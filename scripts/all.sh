#!/bin/sh

#param1: output file
#param2: mysql user
#param3: mysql password, optional


#file with format: entry_id g_no code_ts g_name g_model origin_country, seperated by \t

if [ $# -lt 3 ]; then
	echo "Missing parameter!"
	echo "Usage: ./all.sh output_file mysql_user mysql_database [mysql_password]"
	exit;
fi;

f="$1.i";
g=$1
user=$2
database=$3
g1="${g}1"
g2="${g}2"
g3="${g}3"

if true; then
sql="set names utf8; select entry_id, g_no, code_ts, g_name, g_model, origin_country from $database.risk_entry_list;"

if [ $# -eq 3 ]; then
	mysql -u$user -e "$sql" > $f;
else
	mysql -u$user -p$4 -e "$sql" > $f;
fi;
fi;

sed '1d' -i $f;

cat $f | tr -d "
" > $g;
python haiguan_norm.py $g $f


# normalize string, detect brand in g_name, 
# collect detected brands and remove them from g_name
awk -F"\t" '{print $4}' $f > $g;
python haiguan_brand.py $g $g1 "name"; 

awk -F"\t" '{print $5}' $f > $g;
python haiguan_brand.py $g $g2 "model";

paste $g1 $g2 > $g3

#brand model commodity gname brand model xxxx gmodel
awk -F"\t" '{if($1=="\\N") brand=$5; else brand=$1; if($2=="\\N") model=$6; else model= $2; printf("%s\t%s\t%s\t%s\n", brand, model, $3, $4)}' $g3 > $g1

# generate commodity id.
# 1. paste together
# 2. make tuple(commodity name + country number) dict, create commodity number
paste $f $g1 > $g2;
python haiguan_commodity.py $g2 $g

sed -i 's/\\N/NULL/g' $g

rm -f $f $g1 $g2 $g3
