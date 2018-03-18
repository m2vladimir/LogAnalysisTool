# LogAnalysisTool
Simple tool for logs analysis.

Binary distribution can be found at the [releases](https://github.com/m2vladimir/LogAnalysisTool/releases)

Input parameters:

Filter parameters - at least one parameter should be specified.
Username
Time period
Pattern for custom message

Grouping parameters - at least one parameter should be specified.
Username
Time unit (e.g. 1 hour, 1 day, 1 month)

Tool scans directory for log files, read log files and filter log records that conform to user
input and produce output given below.

Output:

Single file with all filtered log records; 
Print aggregate statistics - Count of records grouped by grouping input parameters.
