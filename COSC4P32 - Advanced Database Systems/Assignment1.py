# operator to merge two lists
def union(rel_a, rel_b):
    """Relational algebra project set union.
    Finds the union between two relations.
    
    Parameters:
        rel_a : the first relation.
        rel_b : the second relation.
    
    Returns:
        The union between rel_a and rel_b.
    """ 
    return rel_a + rel_b

def union2(rel_a, rel_b):
    """Relational algebra project set union with no duplicates.
    Finds the union between two relations, but without duplicates.

    Parameters:
        rel_a : the first relation.
        rel_b : the second relation.
    
    Returns:
        The union between rel_a and rel_b without reduplication.
    """
    # effectively cast the union as a set, which has no duplicates.
    return set(union(rel_a, rel_b))

def intersection(rel_a, rel_b):
    """Relational algebra project set intersection.
    Finds the intersection between two relations.
    
    Parameters:
        rel_a : the first relation.
        rel_b : the second relation.
    
    Returns:
        The intersect between rel_a and rel_b.
    """ 
    return [i for i in rel_a if i in rel_b]

def set_difference(rel_a, rel_b):
    """Relational algebra project set difference.
    Finds the set difference between two relations.
    
    Parameters:
        rel_a : the first relation.
        rel_b : the second relation.
    
    Returns:
        The set difference between rel_a and rel_b.
    """ 
    return [i for i in rel_a if i not in rel_b]

import operator # needed to map operators to modes.

def selection(relation, column, predicate, mode):
    """Relational algebra project selection.
    Finds rows where some selection criteria is met.
    
    Parameters:
        relation : the relation to apply selection operator on.
        column : the column to select from.
        predicate : what to select from column.
        mode : what kind of arithmetic operator to use for selection.
    
    Returns:
        A relation of rows as selected from the original relation.    
    """ 
    # a dict mapping operators to modes
    op_map = {
        '<':operator.lt,
        '>':operator.gt,
        '<=':operator.le,
        '>=':operator.ge,
        '==':operator.eq,
        '!=':operator.ne
    }
    return [i for i in relation if op_map[mode](i[column], predicate)]

def project(relation, columns):
    """Relational algebra projection operator.
    Projects specified columns from the relation to then be viewed.
    
    Parameters:
       relation : the relation to project.
       columns : list iterable columns to project from relation.
    
    Returns:
        The specified columns from the relation.
    """
    # catch instances where supplied column parameter is out of range.
    try:
        proj = [[row[i] for i in columns] for row in relation]
    except IndexError:
        proj = []
    return proj

def crossproduct(rel_a, rel_b):
    """Finds the cross product between two relations.
    The cross product between two relations is the concatenation of each
    row in the first relation to each row in the second relation. The
    cardinality of the cross product is |rel_a| * |rel_b|.
    
    Parameters:
        rel_a : the first relation.
        rel_b : the second relation.
    
    Returns:
        The cross product between relations rel_a and rel_b.
    """
    return [row_a + row_b for row_a in rel_a for row_b in rel_b]

# create relation as the crossproduct of actors and movies.
cross_prod = crossproduct(actors, movies)

# select the (actor_id,movie_id) pair from the cross product.
act_mov_pairs = project(cross_prod, [0, 4])

# likewise, get the (actor_name,movie_name) pair from the cross product.
acted_in = project(cross_prod, [1, 2, 5])

# get the roles, but we only need id pairs.
role_list = project(roles, [0,1])

# generate the relation we need.
rel = [acted_in[i] for i,j in enumerate(act_mov_pairs) if j in role_list]
