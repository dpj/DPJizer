%
%:- use_module(dpj).
%:- ensure_loaded(dpj).
:- dynamic rplElt/1.
:- consult(dpj).
%['dpj.pl'].
%load_files(['dpj.pl']).

% rplElt(star).
rplElt(r1).
rplElt(r2).
rplElt(r3).
rplElt(r4).

rplElt(idxI).
rplElt(idxJ).


%isRpl(_X).

testUnderIncluded :-
    not(isRpl([])),
    not(isUnder([], [root])),                   % [Check Rpl1]          
    not(isUnder([root], [])),                   % [Check Rpl1]          
    isUnder([r1], [r1]),                        % [Reflexive]           
    isUnder([r1], [root]),                      % [Under Root]          
    isUnder([r1,r2], [r1]),                     % [Under Name]          
    isUnder([r1,star], [r1]),                   % [Under Star]          
    isUnder([root,r1], [r1]),                   % [Strip Root1]         
    isUnder([r1], [root,r1]),                   % [Strip Root2] 
    isUnder([root], [root]),                    % [Check before Stripping]
    isUnder([r1], [r1,star]),                   % [Under Include]       
    isUnder([r1,r2], [r1,star]),                % [Under Include]

    isIncluded([r1],[r1]),                      % [Reflexive]           
    not(isIncluded([], [root])),                % [Check Rpl1]  
    not(isIncluded([root], [])),                % [Check Rpl1]  
    isIncluded([root,r1], [r1]),                % [Strip Root1] 
    isIncluded([r1], [root,r1]),                % [Strip Root2] 
    isIncluded([root], [root]),                 % [Check before Stripping]
    isIncluded([r1], [r1,star]),                % [Include Star]
    isIncluded([r1,r2], [r1,star]),             % [Include Star]
    isIncluded([r1,r2,r3],[r1,star,r3]),        % [Include Name 1]
    isIncluded([r1,r2,r3,r4],[r1,star,r4]),     % [Include Name 2]
    isIncluded([r1,r2,r3,r4],[r1,star,r3,r4]),  % [Include Name 3]
    not(isIncluded([r1,r2,r3,r4],[r1,star,r3])),% [not inlcuded]
    true.


testEffects:-
    RPL1=[root,star],
    %RPL2=[root,r1],
    RPL3=[r1],
    %RPL4=[r2,r1],
    %RPL5=[r1,r1],
    E1=effectReads(RPL1),           isEffect(E1),           % E1=reads Root:*
    E2=effectWrites(RPL1),          isEffect(E2),           % E2=writes Root:*
    Ebad1=effectInvokes(m1, RPL3),  not(isEffect(Ebad1)),
    E3=effectReads(RPL3),           isEffect(E3),           % E3=reads r1
    E4=effectWrites(RPL3),          isEffect(E4),           % E4=writes r1
    Ebad2=effectInvokes(m1, E3),    not(isEffect(Ebad2)),
    E5=effectInvokes(m1, [E3]),     isEffect(E5),           % E5=invokes m1 w. [reads r1]
    E6=effectInvokes(m1, [E4]),     isEffect(E6),           % E6=invokes m1 w. [writes r1]
    E7=effectInvokes(m2, [E3]),     isEffect(E7),           % E7=invokes m2 w. [reads r1]
    isEffect(effectPure),
% Effect Union: U
    effectUnion([E1,E2],[E3], [E1,E2,E3]),
    effectUnion([E1,E2],[E2,E3],[E1,E2,E3]),
% subEffect: c=
    subEffectSets([E1],[E2]),
    not(subEffectSets([E2],[E1])),
    subEffectSets([E3],[E2]),
    subEffectSets([E3],[E1]),
    subEffectSets([E3],[E4]),
    not(subEffectSets([E4],[E3])),
    subEffectSets([E4],[E2]),
    not(subEffectSets([E4],[E1])),
    subEffectSets([E5],[E3]),
    not(subEffectSets([E6],[E3])),
    subEffectSets([E6],[E4]),
    subEffectSets([E5],[E6]),
    subEffectSets([E5],[E7,E6]),
    subEffectSets([E7],[E5,E7,E6]),
    not(subEffectSets([E7],[E5,E6])),
    subEffectSets([effectInvokes(m1,[effectPure])], [effectInvokes(m2,[effectPure])]),
    subEffectSets([], [effectInvokes(m2,[effectPure])]),
    subEffectSets([effectInvokes(m1,[effectPure])], []),
    subEffectSets([effectInvokes(M,[effectReads([r1])])], [effectInvokes(M,[effectWrites([r1])])]),
    true.

testDisjointRpl:-
    not(disjointRpl([r1], [r1])),
    not(disjointRpl([root,r1], [r1])),
    not(disjointRpl([r1], [root,star])),
    disjointRpl([r1], [r2,r1]),
    disjointRpl([r2,r1], [r1]),
    disjointRpl([r2,r1], [r1,r1]),
    disjointRpl([root,star,r1], [r1,r2]),
    disjointRpl([r1,r1],[r1,r1,r1,r1,r1]),
    disjointRpl([r1,r1],[r1,r1,r1]),
    not(disjointRpl([r1,r1],[r1,r1])),
    disjointRpl([root,r1,r2,star,r1], [r1,r2]),
    disjointRpl([root,r1,r2,r4,star,r2], [r1,r2,r3]),
    disjointRpl([root,star], [r1]),
    true.

testNI:-
    RPL1=[root,star],
    %RPL2=[root,r1],
    RPL3=[r1],
    RPL4=[r2,r1],  
    %RPL5=[r1,r1],
    E1=effectReads(RPL1),           isEffect(E1),           % E1=reads Root:*
    E2=effectWrites(RPL1),          isEffect(E2),           % E2=writes Root:*
    Ebad1=effectInvokes(m1, RPL3),  not(isEffect(Ebad1)),
    E3=effectReads(RPL3),           isEffect(E3),           % E3=reads r1
    E4=effectWrites(RPL3),          isEffect(E4),           % E4=writes r1
    Ebad2=effectInvokes(m1, E3),    not(isEffect(Ebad2)),
    E5=effectInvokes(m1, [E3]),     isEffect(E5),           % E5=invokes m1 w. [reads r1]
    E6=effectInvokes(m1, [E4]),     isEffect(E6),           % E6=invokes m1 w. [writes r1]
    E7=effectInvokes(m2, [E3]),     isEffect(E7),           % E7=invokes m2 w. [reads r1]
    E8=effectReads(RPL4),           isEffect(E8),           % E8=reads r2:r1
    E9=effectWrites(RPL4),          isEffect(E9),           % E9=writes r2:r1
    nonInterferingEffectSets([],[]),                        % NI Pure
    nonInterferingEffectSets([effectPure],[]),              % NI Pure
    nonInterferingEffectSets([effectPure],[effectPure]),    % NI Pure
    nonInterferingEffectSets([],[effectPure]),              % NI Pure
    nonInterferingEffectSets([E1, E2, E3, E4, E5, E6, E7, E8, E9], [effectPure]),   % NI Pure
    nonInterferingEffectSets([effectPure], [E1, E2, E3, E4, E5, E6, E7, E8, E9]),   % NI Pure
    nonInterferingEffectSets([E1],[E3]),                    % NI Reads
    nonInterferingEffectSets([E3],[E1]),                    % NI Reads
    nonInterferingEffectSets([E3],[E9]),                    % NI Reads-Writes
    nonInterferingEffectSets([E9],[E3]),                    % NI Writes-Reads (Symmetric)
    nonInterferingEffectSets([E4],[E9]),                    % NI Writes
    nonInterferingEffectSets([E9],[E4]),                    % NI Writes
    nonInterferingEffectSets([E9],[E4,effectPure]),         % NI Writes
    nonInterferingEffectSets([E9,effectPure],[E4,effectPure]),  % NI Writes
    not(nonInterferingEffectSets([E1],[E4])), 
    nonInterferingEffectSets([E5],[effectPure]),            % NI Invokes 1
    nonInterferingEffectSets([E5],[E7]),                    % NI Invokes 1 Symmetric
    nonInterferingEffectSets([E9],[E7]),                    % NI Invokes 1 Symmetric
    true.

