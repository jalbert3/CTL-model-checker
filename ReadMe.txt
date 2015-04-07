My parser expects there to be no lines between sections demonstrated in the test.txt file. 
Additionally, propositions must be 1 character and not the characters: {a,n,d,o,r,E,X,G,U,~}. 

If you would like to use the proposition "true", use the letter 't', as that is included by default on all states.

When reading in propositions and assigning them to particular states, the states they are assigned to must be 
indented by one state.
EX:
p
 0 : 1
 2 : 1
end_p

Finally, you cannot assign negated properties to particular states, if this is desired, you can negate properties within
the CTL formula. The negation of p is simply the absence of it on a particular state. But, you can use negations, they must,
however, be well parenthesized.
EX:

(EGt) when negated must be written as (~(EGt)) as '~' is treated as an operand.

To run, unzip the src file and type "javac Scanner.java"
then pipe in your desired input "java Scanner < test.txt" where test.txt is a provided, working, example.

The output of the model checker is a boolean vector prior to conjunction with initial states and a boolean value
stating whether or not the formula is true, iff. the conjunction of the boolean vector and initial states has at least
one 1.