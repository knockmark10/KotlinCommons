@echo off

:: Fetch tagName
set "tagName=%~1"
goto :tagNameCheck
:tagNamePrompt
set /p "tagName=Enter tag name: "
:tagNameCheck
if "%tagName%"=="" goto :tagNamePrompt

:: Process the params
echo tagName=%tagName%
git tag %tagName%
git push --tags