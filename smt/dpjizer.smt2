;This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
(set-logic UFNIA)
;(set-option MACRO_FINDER true)
;(set-option MBQI true)
;(set-option PULL_NESTED_QUANTIFIERS true)
;(set-option DL_COMPILE_WITH_WIDENING true)
;(set-option DL_UNBOUND_COMPRESSOR false)
(set-info :source |
  DPJizer
|)
(set-info :smt-lib-version 2.0)
(set-info :category "industrial")
(set-info :status unknown)

(declare-sort HeadRPLElement)
(declare-sort NonHeadRPLElement)
(declare-sort RPL)

(declare-fun makeRPL (HeadRPLElement) RPL)
(declare-fun makeRPL (RPL NonHeadRPLElement) RPL)
(declare-fun disjoint (RPL RPL) Bool)
(declare-fun isFullySpecified (RPL) Bool)
;(declare-fun length (RPL) Int)

(declare-const Root HeadRPLElement)
(declare-const Star NonHeadRPLElement)

; The length of an RPL made up of an RPL element is one.
;(assert (forall ((h HeadRPLElement)) (= 1 (length (makeRPL h)))))

; Define the length of nontrivial RPL's recursively.
;(assert (forall ((r RPL) (nh NonHeadRPLElement)) (= (length (makeRPL r nh)) (+ 1 (length r)))))

(assert (forall ((R RPL)) (not (disjoint R R))))

(assert (forall ((R1 RPL) (R2 RPL)) (implies (disjoint R1 R2) (disjoint R2 R1))))

(assert (forall ((r1 NonHeadRPLElement) (r2 NonHeadRPLElement) (R1 RPL) (R2 RPL)) (implies (not (= r1 r2)) (disjoint (makeRPL R1 r1) (makeRPL R2 r2)))))

; Every head RPL element is a fully specified RPL.
(assert (forall ((h HeadRPLElement)) (isFullySpecified (makeRPL h))))

; Define isFullySpecified for an RPL of length more than one recursively.
(assert (forall ((r RPL) (nh NonHeadRPLElement)) (= (and (isFullySpecified r) (not (= Star nh))) (isFullySpecified (makeRPL r nh)))))
;(assert (forall ((r RPL) (nh NonHeadRPLElement)) (=> (isFullySpecified (makeRPL r nh)) (and (not (= nh Star)) (isFullySpecified r)))))
;(assert (forall ((r RPL) (nh NonHeadRPLElement)) (isFullySpecified (makeRPL r nh)) ))
;(assert (forall ((r RPL)) (not (isFullySpecified (makeRPL r Star)))))

(declare-const region1 NonHeadRPLElement)
(declare-const region2 NonHeadRPLElement)

(assert (disjoint (makeRPL (makeRPL Root) region1) (makeRPL (makeRPL Root) region2)))

(check-sat)

(declare-const region3 NonHeadRPLElement)
(declare-const region4 NonHeadRPLElement)

(assert (disjoint (makeRPL (makeRPL Root) region3) (makeRPL (makeRPL Root) region4)))

(check-sat)

(assert (not (disjoint (makeRPL (makeRPL Root) region1) (makeRPL (makeRPL Root) region1))))

(check-sat)

(assert (isFullySpecified (makeRPL (makeRPL Root) region1)))
(assert (not (isFullySpecified (makeRPL (makeRPL (makeRPL Root) region1) Star))))
(assert (not (isFullySpecified (makeRPL (makeRPL (makeRPL (makeRPL Root) region1) Star) region2))))

(check-sat)

(exit)

