;This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.

;MBQI has to be set before set-logic.
(set-option EMATCHING false)
(set-logic QF_UF)
;(set-option MODEL true)
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
(declare-sort RPLElements)

(declare-fun cons (RPLElement RPLElements) RPLElements)
(declare-fun head (RPLElements) RPLElement)
(declare-fun tail (RPLElements) RPLElements)
(declare-fun makeRPL (RPLElements) RPL)
(declare-fun makeRPL (HeadRPLElement) RPL)
(declare-fun makeRPL (HeadRPLElement NonHeadRPLElement) RPL)
(declare-fun isFullySpecified (RPLElements) Bool)
(declare-fun isFullySpecified (RPL) Bool)
(declare-fun head (RPL) RPLElement)
(declare-fun tail (RPL) RPL)
(declare-fun last (RPLElements) RPLElement)
(declare-fun last (RPL) RPLElement)
(declare-fun append (RPLElements RPLElement) RPLElements)
(declare-fun append (RPL RPLElement) RPL)
(declare-fun disjoint (RPL RPL) Bool)
(declare-fun makeRPLElement (NonHeadRPLElement) RPLElement)
(declare-fun makeRPLElement (HeadRPLElement) RPLElement)
(declare-fun isNested (RPL RPL) Bool)

(declare-const nil RPLElements)
(declare-const Root HeadRPLElement)
(declare-const Star NonHeadRPLElement)

; head of RPLElements

(assert (forall ((elt RPLElement)) (= (head (cons elt nil)) elt)))

(assert (forall ((elt1 RPLElement) (elt2 RPLElement)) (= (head (cons elt1 (cons elt2 nil))) elt1)))

; tail of RPLElements

(assert (forall ((elt RPLElement)) (= (tail (cons elt nil)) nil)))

(assert (forall ((elt1 RPLElement) (elt2 RPLElement)) (= (tail (cons elt1 (cons elt2 nil))) (cons elt2 nil))))

; RPLElements = head + tail

(assert (forall ((elts RPLElements)) (= elts (cons (head elts) (tail elts)))))

; head

;(assert (forall ((h HeadRPLElement)) (= (head (makeRPL h) h))))

;(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (head (makeRPL h nh) h))))

; tail

;(assert (forall ((h HeadRPLElement)) (= (head (makeRPL h) h))))

;(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (tail (makeRPL h nh) (makeRPL nh)))))

; append for RPLElements

(assert (forall ((elt RPLElement)) (= (append nil elt) (cons elt nil))))

(assert (forall ((elts RPLElements) (elt RPLElement)) (= (append elts elt) (cons (head elts) (append (tail elts) elt)))))

; isFullySpecified for RPLElements

(assert (forall ((elt RPLElement)) (iff (isFullySpecified (cons elt nil)) (not (= elt (makeRPLElement Star))))))

(assert (forall ((elts RPLElements)) (iff (isFullySpecified elts) (and (not (= (head elts) (makeRPLElement Star))) (isFullySpecified (tail elts))))))

; last for RPLElements

(assert (forall ((elt RPLElement)) (= elt (last (cons elt nil)))))

(assert (forall ((elt RPLElement) (elts RPLElements)) (=> (not (= elts nil)) (= (last (cons elt elts)) (last elts)))))

; makeRPL

;TODO: Add formula to ensure that the RPL is of form h:nh:nh...

(assert (forall ((h HeadRPLElement)) (= (makeRPL h) (makeRPL (cons (makeRPLElement h) nil)))))

(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (makeRPL h nh) (makeRPL (cons (makeRPLElement h) (cons (makeRPLElement nh) nil))))))

; Currently, Root is our only HeadRPLElement (See http://stackoverflow.com/q/6932350/130224).
;(assert (forall ((h HeadRPLElement)) (= h Root))

; last RPL element of an RPL element of lenght one
;(assert (forall ((h HeadRPLElement)) (= (last (makeRPL h)) (makeRPLElement h))))

; last RPL element of an RPL element of lenght two
;(assert (forall ((h HeadRPLElement) (nh NonHeadRPLElement)) (= (last (makeRPL h nh)) (makeRPLElement nh))))

; append for RPL

;(assert (forall ((elts RPLElements) (elt RPLElement)) (= (append (makeRPL elts) elt) (makeRPL (append elts elt)))))

; isFullySpecified for RPL

(assert (forall ((elts RPLElements)) (iff (isFullySpecified elts) (isFullySpecified (makeRPL elts)))))

; last for RPL

(assert (forall ((elts RPLElements)) (= (last elts) (last (makeRPL elts)))))

;; Nesting

; UNDER-NAME
;(assert (forall ((R1 RPL) (R2 RPL) (elt RPLElement)) (=> (isNested R1 R2) (isNested (append R1 elt) R2))))

;; Disjointness

; anti-reflexivity
(assert (forall ((R RPL)) (not (disjoint R R))))

; symmetry
(assert (forall ((R1 RPL) (R2 RPL)) (=> (disjoint R1 R2) (disjoint R2 R1))))

; disjointness from right
(assert (forall ((R1 RPL) (R2 RPL)) (=> (not (= (last R1) (last R2))) (disjoint R1 R2))))

(declare-const nh1 NonHeadRPLElement)
(declare-const nh2 NonHeadRPLElement)
(declare-const h1 HeadRPLElement)

(push)
(assert (and (= nh1 nh2) (disjoint (makeRPL Root nh1) (makeRPL Root nh2))))
(check-sat)
(pop)

(push)
(assert (= (last (makeRPL Root)) (makeRPLElement Root)))
(assert (= (last (makeRPL Root nh1)) (makeRPLElement nh1)))
(assert (= (last (makeRPL Root Star)) (makeRPLElement Star)))
(check-sat)
(pop)

(push)
(assert (isFullySpecified (makeRPL Root nh1)))
(assert (not (isFullySpecified (makeRPL Root Star))))
(check-sat)
(pop)

(push)
(assert (disjoint (makeRPL Root nh1) (makeRPL Root nh2)))
(assert (not (disjoint (makeRPL Root nh1) (makeRPL Root))))
(assert (not (disjoint (makeRPL Root) (makeRPL Root))))
(assert (disjoint (makeRPL Root) (makeRPL h1)))
(check-sat)
(pop)

;(model)

(exit)

