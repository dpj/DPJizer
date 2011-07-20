(set-logic UFNIA)
(set-info :source |
  DPJizer
|)
(set-info :smt-lib-version 2.0)
(set-info :category "industrial")
(set-info :status unknown)

(declare-sort HeadRPLElement)
(declare-sort NonHeadRPLElement)
;(declare-sort RPLElement)
(declare-sort RPL)

(declare-fun makeRPL (HeadRPLElement) RPL)
(declare-fun makeRPL (RPL NonHeadRPLElement) RPL)
(declare-fun disjoint (RPL RPL) Bool)

(declare-const Root HeadRPLElement)

(assert (forall ((R RPL)) (not (disjoint R R))))

;(assert (forall ((R1 RPL) (R2 RPL) (r RPLElement)) (implies (= (makeRPL R1 r) R2) (not (= r Root)))))

(assert (forall ((R1 RPL) (R2 RPL)) (implies (disjoint R1 R2) (disjoint R2 R1))))

(assert (forall ((r1 NonHeadRPLElement) (r2 NonHeadRPLElement) (R1 RPL) (R2 RPL)) (implies (not (= r1 r2)) (disjoint (makeRPL R1 r1) (makeRPL R2 r2)))))

(declare-const region1 NonHeadRPLElement)
(declare-const region2 NonHeadRPLElement)

(assert (disjoint (makeRPL (makeRPL Root) region1) (makeRPL (makeRPL Root) region2)))

(check-sat)

(declare-const region3 NonHeadRPLElement)
(declare-const region4 NonHeadRPLElement)

(assert (disjoint (makeRPL (makeRPL Root) region3) (makeRPL (makeRPL Root) region4)))

(check-sat)

(model)

(assert (not (disjoint (makeRPL (makeRPL Root) region1) (makeRPL (makeRPL Root) region1))))

(check-sat)

(exit)

