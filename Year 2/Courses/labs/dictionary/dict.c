#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "dict.h"
#define UNIMPLEMENTED {fprintf(stderr,"You have not implemented %s yet\n",__FUNCTION__);exit(EXIT_FAILURE);}

dict createdict()
{
	return NULL;
}

dict addword(char w[], dict d )
{

	if (d == NULL)
	{
		d = (dict) malloc(sizeof(struct dictionary)); // d is a pointer to a dictionary
		d->left = NULL;
		d->right = NULL;
		strcpy(d->theword,w);
	}
	else
	{
		int comp = strcmp(w,(*d).theword);

		if (comp<0)
		{
			((*d).left) = addword(w,(*d).left);
		}
		else if (comp>0)
		{
			((*d).right) = addword(w,(*d).right);
		}
		else
		{//in the case that the word already exists, return the dictionary unchanged
			printf("%s already exists; dictionary unchanged\n",(*d).theword);
   		}
	}
	return d;

}

dict readdict(FILE * in,  dict d)
{
	char temp[MAXWORDSIZE];

	while(fscanf(in,"%99s",temp) == 1)
	{
		d=addword(temp,d);
		printf("%s added\n",temp);
	}

	return d;
}

void writedict(FILE * out ,dict d )
{
	if (d==NULL)
	{
		fprintf(out,"\n");
	}
	else
	{
		if (d->left != NULL)
		{
			writedict(out,d->left);
		}

		fprintf(out,"%s\n",d->theword);

		if (d->right != NULL)
		{
			writedict(out,d->right);
		}
	}
}

int lookup( char w[] , dict d)
{
	if (d == NULL)
	{
		return 0;
	}
	else
	{
		int comp = strcmp((*d).theword,w);
		int ans;

		if (comp<0)
		{
			ans = lookup(w,(*d).left);
		}
		else if (comp>0)
		{
			ans = lookup(w,(*d).right);
		}
		else
		{
			ans = 1;
		}
		return ans;
	}

}

int countwords(dict d)
{
	if (d==NULL)
	{
		return 0;
	}
	else
	{
		return (1+(countwords((*d).left))+(countwords((*d).right)));
	}
}

int findword(dict d, int n, char w [] )
{
	int currNodeInd = countwords(d->left)+1;

	if (n<currNodeInd)
	{//the nth node is in the left subtree
		return findword(d->left,n,w);
	}
	else if (n>currNodeInd)
	{//the nth node is in the right subtree
		if (d->right==NULL)
		{// n is an invalid index
			printf("Error, there aren't %i words
				in this dictionary. Dictionary unchanged.\n",n);
			return 0;
		}
		return findword(d->right,n-currNodeInd,w);
		}
	else
	{//current node containts the required word
		strcpy(w,d->theword);
		return 1;
	}
}

dict getRightmost(dict d)
{//d cannot be null
	if(d->right==NULL)
	{
		return d;
	}
	else
	{
		return getRightmost(d->right);
	}

}


dict deleteword(char w[] , dict d)
{
	int comp = strcmp(w,d->theword);

	if (comp == 0)
	{//i.e current node is the required one to be deleted
		dict dNew = d->left;
		if (d->left == NULL)
		{
			dNew = d->right;
		}
		else
		{
			dict rmost = getRightmost(d->left);
			rmost->right = d->right;
		}

		free(d);
		return dNew;
	}
	else
	{
		if (comp<0)
		{//w is in the left subtree
			d->left = deleteword(w,d->left);
		}
		else if (comp>0)
		{//w is in the right subtree
			d->right = deleteword(w,d->right);
		}
		return d;
	}
}


void deletedict( dict d)
{
	if (d != NULL)
	{
		free(d);
		deletedict(d->left);
		deletedict(d->right);
	}
}

