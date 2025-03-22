sig Sig {}

sig ProductType {
	primitiveTypes : seq Sig,
}
sig Type {
	entries : some ProductType,
	arity : one Int
} {
	all e : entries | #e.primitiveTypes = arity
	arity > 0
}

pred join( l, r, c : ProductType ) {
}
pred join( l, r, c : Type ) {
	
}

pred intersect( a, b, c : Type ) {
	
}

pred union( a, b, c : Type ) {

	
}

pred product( a, b, c : ProductType ) {
	c.primitiveTypes = a.primitiveTypes.append[ b.primitiveTypes ]
}
pred product( a, b, c : Type ) {
	c.entries = { p : ProductType, aa : a.entries, bb: b.entries | product[aa,bb,p] }.univ.univ	
}

pred transpose( type, result : ProductType ) {
	type.primitiveTypes[0] = result.primitiveTypes[1]
	type.primitiveTypes[1] = result.primitiveTypes[0]
}

pred transpose( type, result : Type ) {
	type.arity = 2
	result.arity = 2
	result.entries = { p : type.entries, r : ProductType | transpose[p,r] }[univ]
}

pred diversity {
	all p : ProductType | # p.primitiveTypes = #p.primitiveTypes[Int] and # p.primitiveTypes < 4
}

run {  
	diversity	
	some a,b,c : Type | product[a,b,c] and c.arity > 2
} for 5
