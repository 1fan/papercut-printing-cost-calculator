# papercut-printing-cost-calculator
PaperCut Code Challenge - Printing Job Cost Calculator

PaperSize (Enum):A4;

JobType (Enum): SINGLE_SIDE,  DOUBLE_SIDE;

ColorType(Enum): BLACK_WHITE, COLOUR;

Price (Should be configuable - read from configuration file):
* Single side: 15c for B_W, 25c for Colour;
* Double side: 10c  for B_W, 20c for Colour;

Requirement:
* Read printing job details from file; (each row is a job --> a Job entity)
* Output details of each job, and calculate cost, and print into the console
* Output theh total cost of all jobs to the console
* Unit Tests


# Assumption:
* currency smallest unit digit is 2.