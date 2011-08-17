;This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.

;MBQI has to be set before set-logic.
;(set-option EMATCHING false)
(set-logic QF_UF)
(set-option MODEL true)
(set-info :source |
  DPJizer
|)
(set-info :smt-lib-version 2.0)
(set-info :category "industrial")
;(set-info :status unknown)

(declare-sort RPLElement)
(declare-sort HeadRPLElement)
(declare-sort NonHeadRPLElement)
(declare-sort RPL)

(declare-fun makeRPL (HeadRPLElement) RPL)
(declare-fun makeRPL (HeadRPLElement NonHeadRPLElement) RPL)
(declare-fun isFullySpecified (NonHeadRPLElement) Bool)
(declare-fun isFullySpecified (RPL) Bool)
(declare-fun head (RPL) RPLElement)
(declare-fun tail (RPL) RPL)
(declare-fun last (RPL) RPLElement)
(declare-fun append (RPL NonHeadRPLElement) RPL)
(declare-fun disjoint (RPL RPL) Bool)
(declare-fun makeRPLElement (NonHeadRPLElement) RPLElement)
(declare-fun makeRPLElement (HeadRPLElement) RPLElement)
(declare-fun isNested (RPL RPL) Bool)

(declare-const Root HeadRPLElement)
(declare-const Star NonHeadRPLElement)

;; Define makeRPLElement as a one-to-one function.

(assert (forall ((h1 HeadRPLElement) (h2 HeadRPLElement)) (iff (= h1 h2) (= (makeRPLElement h1) (makeRPLElement h2)))))

(assert (forall ((nh1 NonHeadRPLElement) (nh2 NonHeadRPLElement)) (iff (= nh1 nh2) (= (makeRPLElement nh1) (makeRPLElement nh2)))))

(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (not (= (makeRPLElement h) (makeRPLElement nh)))))

; head

;(assert (forall ((h HeadRPLElement)) (= (head (makeRPL h) h))))

;(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (head (makeRPL h nh) h))))

; tail

;(assert (forall ((h HeadRPLElement)) (= (head (makeRPL h) h))))

;(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (tail (makeRPL h nh) (makeRPL nh)))))

;; makeRPL

; Define makeRPL as a one-to-one function.

(assert (forall ((h1 HeadRPLElement) (h2 HeadRPLElement)) (iff (= h1 h2) (= (makeRPL h1) (makeRPL h2)))))

(assert (forall ((h1 HeadRPLElement) (nh1 NonHeadRPLElement) (h2 HeadRPLElement) (nh2 NonHeadRPLElement)) (iff (and (= h1 h2) (= nh1 nh2)) (= (makeRPL h1 nh1) (makeRPL h2 nh2)))))

; Currently, Root is our only HeadRPLElement (See http://stackoverflow.com/q/6932350/130224).
;(assert (forall ((h HeadRPLElement)) (= h Root))

;; append

(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (append (makeRPL h) nh) (makeRPL h nh))))

; isFullySpecified

(assert (forall ((nh NonHeadRPLElement)) (= (isFullySpecified nh) (not (= nh Star)))))

(assert (forall ((h HeadRPLElement)) (isFullySpecified (makeRPL h))))

(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (iff (isFullySpecified (makeRPL h nh)) (isFullySpecified nh))))

;; last

(assert (forall ((h HeadRPLElement)) (= (makeRPLElement h) (last (makeRPL h)))))

(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (makeRPLElement nh) (last (makeRPL h nh)))))

;; Nesting

; UNDER-NAME
; (assert (forall ((R1 RPL) (R2 RPL) (elt RPLElement)) (=> (isNested R1 R2) (isNested (append R1 elt) R2))))

;; Disjointness

; anti-reflexivity
(assert (forall ((R RPL)) (not (disjoint R R))))

; symmetry
(assert (forall ((R1 RPL) (R2 RPL)) (iff (disjoint R1 R2) (disjoint R2 R1))))

; disjointness from right
(assert (forall ((R1 RPL) (R2 RPL)) (=> (not (= (last R1) (last R2))) (disjoint R1 R2))))

(declare-const nh1 NonHeadRPLElement)
(declare-const nh2 NonHeadRPLElement)
(declare-const h1 HeadRPLElement)

(push)
(assert (and (= nh1 nh2) (= (makeRPLElement nh1) (makeRPLElement nh2))))
(check-sat) ;sat
(pop)

(push)
(assert (and (not (= nh1 nh2)) (= (makeRPLElement nh1) (makeRPLElement nh2))))
(check-sat) ;unsat
(pop)

(push)
(assert (= (last (makeRPL Root)) (makeRPLElement Root)))
(assert (= (last (makeRPL Root nh1)) (makeRPLElement nh1)))
(assert (= (last (makeRPL Root Star)) (makeRPLElement Star)))
(check-sat) ;sat
(pop)

(push)
(assert (isFullySpecified (makeRPL Root nh1)))
(assert (not (isFullySpecified (makeRPL Root Star))))
(check-sat) ;sat
(pop)

(push)
(assert ((isFullySpecified (makeRPL Root Star))))
(check-sat) ;unsat
(pop)

(push)
(assert (and (= nh1 nh2) (disjoint (makeRPL Root nh1) (makeRPL Root nh2))))
(check-sat) ;unsat
(pop)

(push)
(assert (not (disjoint (makeRPL Root nh1) (makeRPL Root))))
(check-sat) ;unsat
(pop)

(push)
(assert (disjoint (makeRPL Root nh1) (makeRPL Root nh2)))
(assert (not (disjoint (makeRPL Root) (makeRPL Root))))
(assert (disjoint (makeRPL Root) (makeRPL h1)))
(check-sat) ;sat
(pop)

(push)
(assert (= (append (makeRPL Root) nh1) (makeRPL Root nh1)))
(check-sat) ;sat
(pop)

(push)
(assert (and (not (= nh1 nh2)) (= (append (makeRPL Root) nh1) (makeRPL Root nh2))))
(check-sat) ;unsat
(pop)

;;(push)
;;(assert (isNested (makeRPL Root nh1) (makeRPL Root)))
;;(check-sat)
;;(pop)

(exit)

