# codeChallenge

Approach Summary

I used java to solve this code challenge. Firstly it reads the given inactivity_time.txt to verify the inactivity time. Then it reads each line (except the header) of log.csv to analyze each log information and update user information.The User.java is the user object I created to manage user information.Everytime an user session is expired, the corresponding user information will be calculated corecctly and write into the output file.After all the log being read, the ongoing user sessions will be again calculate and stored into the output file.

My personal Assumption: I assumed the session of a user can not be longer than one month because I think this is reasonable session time for a real human user. However, if the clients would request me to increase that limit, I can modify its limit way up to 68.096259734906 year which then will be the limitation of the 2038 problem. 


Dependencies

1.Eclipse on Win10 as programming platform

2.Linux Ubuntou 18.04 LTS to edit and test run.sh and java files

Run Instruction

1. Download the codeChallenge repo
2. Change the path in run.sh to where you downloaded the repo on your Linux system (path to parent foldere of codeChallenge folder).
3. Change static varieble path in createSessionization.java to where you downloaded the repo on your Linux system (path to parent foldere of codeChallenge folder).
4. At last, "cd <parent folder of codeChallenge>" and execure run.sh by "sh run.sh". The output file is in output folder as sessionization.txt.
