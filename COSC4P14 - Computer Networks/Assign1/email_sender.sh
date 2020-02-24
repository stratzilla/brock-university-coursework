#!/bin/bash

{
	# add sleep cycles to wait for responses
	sleep 2; echo "HELO localhost"; sleep 2;
	echo "MAIL FROM: <example@email.com>"; sleep 2;
	echo "RCPT TO: <example@email.com>"; sleep 2;
	echo "DATA"; sleep 2; # begin inserting e-mail headers
	echo "From: Name <example@email.com>";
	echo "To: Name <example@email.com>";
	echo "Date: $(date +%y%m%d)";
	echo "Subject: This is 4P14 A1 Q10.";
	echo "Content-Type: multipart/mixed; boundary=\"BOUNDARY\"";
	echo "--BOUNDARY";
	echo "Content-Type: text/plain; charset=UTF-8; format=flowed";
	echo "Content-Disposition: inline";
	echo ""; echo "This is a message body."; # need carriage return
	echo "--BOUNDARY";
	echo "Content-Type: file --mime-type -b iagdv.png; name=iagdv.png";
	echo "Content-Transfer-Encoding: base64"; # send as base64
	echo "Content-Disposition: attachment; filename=\"iagdv.png\"";
	echo ""; cat iagdv.png | base64;
	echo "--BOUNDARY";
	echo "."; sleep 2;
	echo "quit";
} | nc smtp.localhost 25