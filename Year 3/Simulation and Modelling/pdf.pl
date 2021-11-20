#!/usr/bin/perl
###########################################################
### pdf.pl ############################################
### v 1.0 ###############################################
### 11/11/2003 Uli Harder ###################################
###########################################################
### This program can produce an aggregated time series
### 
###########################################################
### Some useful defintions ################################
use strict;
###########################################################
### Input #################################################
my $InputFile = shift;

my $Unit = 1;
### Output ###############################################

my $OutFile = $InputFile  ."-pdf.dat";

### Other useful things
#open(IN,"zcat $InputFile|");
open(IN,"cat $InputFile|");

my @FirstLine = split(/\s+/, <IN>);


my @Changes = ();
my $TotalArrivals = 0;
my $Ignored = 0;

my $Change =  $FirstLine[$#FirstLine]; 
my $MaxChange = $Change;
my $MinChange = $Change;

my $Mean = $Change;
my $Var = $Change**2;
$TotalArrivals++;


LOOP: while (<IN>)
{
    my (@line) = split(/\s+/, $_);
    my $Change =$line[$#line]; 
    if ($Change eq "-") {$Change = 0;}
    next LOOP if ($Change eq "-");
    
    if ($Change > $MaxChange){$MaxChange = $Change;}
    elsif ($Change < $MinChange){$MinChange = $Change;}
    $Mean += $Change;
    $Var += $Change**2;
    $TotalArrivals++;
}
close IN;


print STDERR "Finished first data input.\n";
if ($TotalArrivals > 2)
{
#    $Mean /= $TotalArrivals;
#    $Var += $TotalArrivals * $Mean **2;
#    $Var /= ($TotalArrivals - 1);
#    $Var = $Var/$TotalArrivals - $Mean**2;
}
#print STDERR "Total Events: $TotalArrivals\n";
#print STDERR "Mean : $Mean, Variance: $Var\n";
#print STDERR "Max. Change: $MaxChange Min. Change: $MinChange \n";



my $Barrier = 0;
if (abs($MinChange) > $MaxChange) {$Barrier = abs($MinChange);}
else {$Barrier = $MaxChange;}
#open(IN,"zcat $InputFile|");
open(IN,"cat $InputFile|");

@FirstLine = split(/\s+/, <IN>);



$Ignored = 0;


my $Bins = 100;
if ($Barrier < 1) {print STDERR "The max. value is smaller than 1. Program will fail.\n";}


$MaxChange = $Barrier;
my $Step = $Barrier**(1/$Bins);
print STDERR "Log step size: $Step.\n";

my @PosHistogram = ();

my $Huge = 0;
 LOOP: while (<IN>)
{
    my (@line) = split(/\s+/, $_);
    my $ThisValue = my $Change = $line[$#line]; 

#    print "$Change \n";
    if ($Change eq "-") {$Change = 0;}
    next LOOP if ($Change eq "-");
    my $Change = $ThisValue/$Unit;
#    print "$Change \n";
    if($Change > $MaxChange){$Huge++;}
    else
    {
	#print "Hello\n";
	my $Found = 0;
	my $i =1;
	my $Left = 0;
	my $Right =  $Step**$i;
	while ($Found == 0)
	{
	    if($Change >= $Left  && $Change <  $Right ){$Found = 1;}
	    else{}
	    $i++;
	    $Left = $Right;
	    $Right = $Step**$i;
	    
	}

	if($Found == 1){$PosHistogram[$i-1]++;}
	}


    ### done
}
close IN;
print STDERR "Finished second data input.\n";


if ($TotalArrivals > 2)
{
    my $sigma = sqrt($Var);
    open(OUT,">$OutFile");
    my $j  = 1;
    my $Left = 0;
    my $Right = ($Step**$j);
    while ($j < $#PosHistogram)
    {
	if ($PosHistogram[$j] > 0)
	{
	    my $Probability = $PosHistogram[$j]/$TotalArrivals/($Right - $Left) ;
	    my $X = $Left + ($Right-$Left)/2;

#	    print OUT "$X  $Probability $Left $Right\n";
	    print OUT "$X  $Probability\n";
	}
	$j++;
	$Left = $Right;
	$Right = ($Step**$j);	
    }
    close OUT;
	

    print STDERR "Huge: ".  $Huge/$TotalArrivals . "\n";


    print STDERR "The end. \n";
    
}
else
{
    print STDERR "Not enough arrivals!\n";
}
__END__

















