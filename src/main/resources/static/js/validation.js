/**
 * 
 */

let name =document.getElementById("name");
let fathername=document.getElementById("fathername");
let emailid=document.getElementById("emailid");
let dob=document.getElementById("dateofBirth");
let mobile=document.getElementById("contactno");
let passingper=document.getElementById("passed_exam");
let engpassing=document.getElementById("eng_passed_exam");
let minheight=document.getElementById("height");



let lblen=document.getElementById("verrorn");
let lblef=document.getElementById("verrorf");
let lblee=document.getElementById("verrore");
let lbledob=document.getElementById("verrordob");
let lblemo=document.getElementById("verrormo");
let lblendet=document.getElementById("verrordet");
let lblenpp=document.getElementById("verrorpp");
let lblenepp=document.getElementById("verrorepp");
let lblenh=document.getElementById("verrorh");










name.addEventListener("blur",function(e){
	var re = /[a-zA-Z][a-zA-Z ]+[a-zA-Z]$/;
	mesg="invalid Name";
	errotoogler(lblen,re,e,mesg);
	
});

fathername.addEventListener("blur",function(e){
	var re = /[a-zA-Z][a-zA-Z ]+[a-zA-Z]$/;
	mesg="invalid Name";
	errotoogler(lblef,re,e,mesg);
	
});

emailid.addEventListener("blur",function(e){
	var re = /([A-Z0-9a-z_-][^@])+?@[^$#<>?]+?\.[\w]{2,4}/;
	mesg="invalid email";
	errotoogler(lblee,re,e,mesg);
	
});

mobile.addEventListener("blur",function(e){
	var re = /^[6-9]\d{9}$/;
	mesg="invalid mobile number";
	errotoogler(lblemo,re,e,mesg);
	
});





function errotoogler(errordiv,regx,e,msg)
{
let lblen=errordiv;
	
	console.log("value"+e.target.value);
	var reg=regx.test(e.target.value);
	if(!reg)
		{
		lblen.innerHTML =msg;
		lblen.style.color="red";
		e.target.style.borderColor ="red";
		e.target.value = "";
		}
	else
		{
		lblen.innerHTML ="";
		lblen.style.color="";
		e.target.style.borderColor ="";
		}



}


